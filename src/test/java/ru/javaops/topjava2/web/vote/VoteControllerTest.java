package ru.javaops.topjava2.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.web.AbstractControllerTest;
import ru.javaops.topjava2.web.GlobalExceptionHandler;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.AbstractTestData.*;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.rest1;
import static ru.javaops.topjava2.web.user.UserTestData.user2;
import static ru.javaops.topjava2.web.vote.VoteTestData.*;


//https://www.baeldung.com/spring-tests-override-properties
@TestPropertySource(properties = {
        "timeLimit.hour=23",
        "timeLimit.min=59"
})

class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteController.REST_URL + '/';
    private static final LocalTime TIME_LIMIT = LocalTime.of(11,00);
    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL + VOTE1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(vote1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    //Todo добавить что админ может получать любой голос
    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotOwn() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL + (VOTE1_ID + 1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        if (LocalTime.now().isBefore(TIME_LIMIT)) {
            Vote updated = getUpdated();
            updated.setId(null);
            perform(MockMvcRequestBuilders
                    .patch(REST_URL + VOTE3_ID)
                    .param("restaurantId", Integer.toString(REST2_ID)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            updated = voteRepository.getById(VOTE3_ID);
            MATCHER.assertMatch(updated, getUpdated());
            assertEquals(REST2_ID, updated.getRestaurant().id());
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateLate() throws Exception {
        //ToDo как подменить глобальную переменную на время выполнения тестирования вынести в параметры
        if (LocalTime.now().isAfter(TIME_LIMIT)) {
            perform(MockMvcRequestBuilders
                    .patch(REST_URL + VOTE3_ID)
                    .param("restaurantId", Integer.toString(REST2_ID)))
                    .andDo(print())
                    .andExpect(status().isLocked());
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateNotOwn() throws Exception {
        perform(MockMvcRequestBuilders
                .patch(REST_URL + (VOTE1_ID + 3))
                .param("restaurantId", Integer.toString(REST2_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateNotToday() throws Exception {
        perform(MockMvcRequestBuilders
                .patch(REST_URL + VOTE1_ID)
                .param("restaurantId", Integer.toString(REST2_ID)))
                .andDo(print())
                .andExpect(status().isLocked());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateNotFoundVote() throws Exception {
        perform(MockMvcRequestBuilders
                .patch(REST_URL + NOT_FOUND)
                .param("restaurantId", Integer.toString(REST2_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateNotFoundRestaurant() throws Exception {
        perform(MockMvcRequestBuilders
                .patch(REST_URL + VOTE3_ID)
                .param("restaurantId", Integer.toString(NOT_FOUND)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHERTO.contentJson(votesTo));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllByUser() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUnAuth() throws Exception {
        perform(MockMvcRequestBuilders
                .get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "user2@yandex.ru")
    void createWithLocation() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", Integer.toString(REST1_ID)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote newVote = new Vote(null, LocalDate.now(), null, null);
        Vote created = MATCHER.readFromJson(action);
        int newId = created.getId();
        newVote.setId(newId);
        MATCHER.assertMatch(created, newVote);
        Vote fromDb = voteRepository.getByIdWithRestaurantAndUser(newId).orElseThrow();
        MATCHER.assertMatch(fromDb, newVote);
        assertEquals(fromDb.getUser().getId(), user2.getId());
        assertEquals(fromDb.getRestaurant().getId(), rest1.getId());
    }

    @Test
    @WithUserDetails(value = "user2@yandex.ru")
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", "fdfd"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders
                .post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", Integer.toString(REST1_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_VOTE)));

    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createNotFoundRestaurant() throws Exception {
        perform(MockMvcRequestBuilders
                .post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", Integer.toString(NOT_FOUND)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createLate() throws Exception {
        if (LocalTime.now().isAfter(TIME_LIMIT)) {
            perform(MockMvcRequestBuilders
                    .post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("restaurantId", Integer.toString(REST1_ID)))
                    .andDo(print())
                    .andExpect(status().isLocked());
        }
    }
}