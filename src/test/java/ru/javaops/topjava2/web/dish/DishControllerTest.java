package ru.javaops.topjava2.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.repository.DishRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.web.AbstractControllerTest;
import ru.javaops.topjava2.web.GlobalExceptionHandler;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.util.JsonUtil.writeValue;
import static ru.javaops.topjava2.web.dish.DishTestData.*;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.rest1;

class DishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/api/restaurants/";

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    DishRepository dishRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Dish newDish = new Dish(null,dish11.getName(),dish11.getPrice(),dish11.getForDate());
        perform(MockMvcRequestBuilders.post(REST_URL + REST1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_DISH)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST1_ID + "/dishes/" + DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(dish1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getInvalid() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + (REST1_ID + 1) + "/dishes/" + DISH1_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        // https://jira.spring.io/browse/SPR-14472
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST1_ID + "/dishes/" + NOT_FOUND))
                .andDo(print())
                .andExpect((status().isUnprocessableEntity()));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST1_ID + "/dishes/" + DISH1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + REST1_ID + "/dishes/" + DISH10_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.findById(DISH10_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + REST1_ID + "/dishes/" + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteByUSer() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + REST1_ID + "/dishes/" + DISH10_ID))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + REST1_ID + "/dishes/" + DISH1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void update() throws Exception {
        Dish updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL + REST1_ID + "/dishes/" + DISH10_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MATCHER.assertMatch(dishRepository.findById(DISH10_ID).orElseThrow(), getUpdated());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateByUser() throws Exception {
        Dish updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL + REST1_ID + "/dishes/" + DISH10_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Dish updated = getUpdated();
        updated.setId(DISH10_ID);
        updated.setName(dish11.getName());
        updated.setForDate(dish11.getForDate());
        updated.setRestaurant(rest1);
        perform(MockMvcRequestBuilders.put(REST_URL + REST1_ID + "/dishes/" + DISH10_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_DISH)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void updateInvalid() throws Exception {
        Dish updated = getUpdated();
        updated.setId(DISH10_ID);
        updated.setName("");
        updated.setRestaurant(rest1);
        perform(MockMvcRequestBuilders.put(REST_URL + REST1_ID + "/dishes/" + DISH10_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void updateInvalidRestaurant() throws Exception {
        Dish updated = getUpdated();
        updated.setId(DISH10_ID);
        updated.setRestaurant(rest1);
        perform(MockMvcRequestBuilders.put(REST_URL + (REST1_ID + 1) + "/dishes/" + DISH10_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
   //ToDo create after 11 o'clock
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + REST1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isCreated());

        Dish created = MATCHER.readFromJson(action);
        int newId = created.getId();
        newDish.setId(newId);
        MATCHER.assertMatch(created, newDish);
        MATCHER.assertMatch(dishRepository.getById(newId), newDish);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Dish newDish = getNew();
        newDish.setName("");
        perform(MockMvcRequestBuilders.post(REST_URL + REST1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createWithLocationByUser() throws Exception {
        Dish newDish = getNew();
        newDish.setName("fdsf");
        perform(MockMvcRequestBuilders.post(REST_URL + REST1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }



    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void createNotFoundRestaurant() throws Exception {
        Dish newDish = new Dish(null,dish10.getName(),dish10.getPrice(),dish1.getForDate());
        perform(MockMvcRequestBuilders.post(REST_URL + NOT_FOUND + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }




    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST1_ID + "/dishes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(dishesOfRestaurant1));
    }
}