package ru.javaops.topjava2.web.vote;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.web.AbstractControllerTest;

public abstract class AbstractVoteControllerTest  extends AbstractControllerTest {
    protected static final String REST_URL = VoteController.REST_URL + '/';


    @Autowired
    protected VoteRepository voteRepository;
}
