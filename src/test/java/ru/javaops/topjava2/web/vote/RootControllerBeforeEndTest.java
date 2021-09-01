package ru.javaops.topjava2.web.vote;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.web.GlobalExceptionHandler;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.AbstractTestData.USER1_MAIL;
import static ru.javaops.topjava2.web.user.UserTestData.user1;
import static ru.javaops.topjava2.web.vote.RootTestData.*;

//https://www.baeldung.com/spring-tests-override-properties
@TestPropertySource(properties = {
        "limit-time.vote=23:59"
})

class RootControllerBeforeEndTest extends AbstractVoteControllerTest{

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("pageNumber","0")
                .param("pageSize","10"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHERMENUTO.contentJson(allMenuTosForToday));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("pageNumber","0")
                .param("pageSize","10"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getBadRequest() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("pageNumber","dfs")
                .param("pageSize","sdf"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getResult() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/vote/result"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHERRESTAURANTTO.contentJson(voteResult));
    }

    @Test
    void getResultUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/vote/result"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test

    @WithUserDetails(value = USER2_MAIL)
    void createVoteWithLocation() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + "/vote")
                .param("restaurantId",Integer.toString(REST1_ID)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote created = MATCHERVOTE.readFromJson(action);
        int newId = created.getId();
        Vote newVote = new Vote(newId, LocalDate.now(), null,null);
        newVote.setRegTime(created.getRegTime());
        MATCHERVOTE.assertMatch(created,newVote);
        MATCHERVOTE.assertMatch(voteRepository.getById(newId),newVote);

    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void createNotFound() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + "/vote")
                .param("restaurantId",Integer.toString(NOT_FOUND)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void createDuplicate() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + "/vote")
                .param("restaurantId",Integer.toString(REST1_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_VOTE)));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getVote() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/vote/by"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHERVOTE.contentJson(vote3));
    }

    @Test
    void getVoteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/vote/by"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }


    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getAllVotes() throws Exception {
         perform(MockMvcRequestBuilders.get(REST_URL + "/vote/user/history")
                .param("startDate", "")
                .param("endDate",""))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHERVOTE.contentJson(List.of(vote1,vote3)));
            }

    @Test
    void getAllVotesUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/vote/user/history")
                .param("startDate", "")
                .param("endDate",""))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getAllVotesBadRequest() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/vote/user/history")
                .param("startDate", "sadas")
                .param("endDate","asdas"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getResultHistory() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL + "/vote/result/history")
                .param("startDate", "")
                .param("endDate",""))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHERRESTAURANTTO.contentJson(historyResult));
    }

    @Test
    void getResultHistoryUnAuth() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL + "/vote/result/history")
                .param("startDate", "")
                .param("endDate",""))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getResultHistoryBadRequest() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL + "/vote/result/history")
                .param("startDate", "dsfs")
                .param("endDate","dfsdfds"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/vote")
                .param("restaurantId",Integer.toString(REST2_ID)))
                .andExpect(status().isNoContent())
                .andDo(print());

        Vote updated = voteRepository.findByUserId(user1.getId()).get();
        Assertions.assertEquals(updated.getRestaurant().getId(),REST2_ID);
    }

    @Test
    void updateUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/vote")
                .param("restaurantId",Integer.toString(REST2_ID)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void updateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "/vote")
                .param("restaurantId",Integer.toString(NOT_FOUND)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

}
















