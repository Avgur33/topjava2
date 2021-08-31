package ru.javaops.topjava2.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.util.JsonUtil.writeValue;
import static ru.javaops.topjava2.web.AbstractTestData.USER1_MAIL;
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
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHERMENUTO.contentJson(allMenuTosForToday));
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void getResult() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/vote/result"))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHERRESTAURANTTO.contentJson(voteResult));
    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    @Transactional
    void createVoteWithLocation() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + "/vote")
                .param("restaurantId","1"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void getVote() {

    }

    @Test
    void getAllVotes() {

    }

    @Test
    void update() {

    }
}