package ru.javaops.topjava2.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.AbstractTestData.*;
import static ru.javaops.topjava2.web.vote.VoteTestData.VOTE3_ID;

@TestPropertySource(properties = {
        "limit-time.vote='00:01"
})
public class VoteControllerAfterEndTest extends AbstractVoteControllerTest {

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void createLate() throws Exception {
        perform(MockMvcRequestBuilders
                .post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restaurantId", Integer.toString(REST1_ID)))
                .andDo(print())
                .andExpect(status().isLocked());
    }

    @Test
    @WithUserDetails(value = USER1_MAIL)
    void updateLate() throws Exception {
            perform(MockMvcRequestBuilders
                    .patch(REST_URL + VOTE3_ID)
                    .param("restaurantId", Integer.toString(REST2_ID)))
                    .andDo(print())
                    .andExpect(status().isLocked());
    }
}
