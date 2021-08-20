package ru.javaops.topjava2.web.vote;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.web.AbstractControllerTest;

class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;


}