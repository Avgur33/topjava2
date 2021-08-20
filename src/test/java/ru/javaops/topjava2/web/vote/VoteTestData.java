package ru.javaops.topjava2.web.vote;

import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.web.AbstractTestData;
import ru.javaops.topjava2.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class VoteTestData extends AbstractTestData {

    public static final MatcherFactory.Matcher<Vote> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "restaurant", "user");
    public static final int VOTE1_ID = 1;

    public static final Vote vote1 = new Vote(VOTE1_ID, LocalDate.now().minusDays(1));
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, LocalDate.now().minusDays(1));
    public static final Vote vote3 = new Vote(VOTE1_ID + 2, LocalDate.now());
    public static final Vote vote4 = new Vote(VOTE1_ID + 3, LocalDate.now());

    public static final List<Vote> votes = List.of(vote1, vote2, vote3,vote4);

}
