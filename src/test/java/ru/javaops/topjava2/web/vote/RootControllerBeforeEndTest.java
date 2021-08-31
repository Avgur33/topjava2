package ru.javaops.topjava2.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
//https://www.baeldung.com/spring-tests-override-properties
@TestPropertySource(properties = {
        "limit-time.vote='23:59"
})

class RootControllerBeforeEndTest extends AbstractVoteControllerTest{

    @Test
    void get() {
    }

    @Test
    void getResult() {
    }

    @Test
    void createVoteWithLocation() {
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