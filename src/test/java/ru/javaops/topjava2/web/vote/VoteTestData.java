package ru.javaops.topjava2.web.vote;

import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.web.AbstractTestData;
import ru.javaops.topjava2.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.VoteUtil.getTos;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.rest1;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.rest2;
import static ru.javaops.topjava2.web.user.UserTestData.*;

public class VoteTestData extends AbstractTestData {

    public static final MatcherFactory.Matcher<Vote> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "restaurant");
    //ToDo заменить MATCHERTO
    public static final MatcherFactory.Matcher<VoteTo> MATCHERTO = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class, "", "");

    public static final Integer VOTE3_ID = 3;
    public static final Vote vote1 = new Vote(VOTE1_ID, LocalDate.now().minusDays(1),USER_ID,rest1);
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, LocalDate.now().minusDays(1), ADMIN_ID,rest2);
    public static final Vote vote3 = new Vote(VOTE1_ID + 2, LocalDate.now(),USER_ID,rest1);
    public static final Vote vote4 = new Vote(VOTE1_ID + 3, LocalDate.now(),ADMIN_ID,rest2);

    public static final List<Vote> votes = List.of(vote1, vote2, vote3,vote4);
    public static Vote getUpdated() { return new Vote(VOTE1_ID + 2,LocalDate.now(),null,null );}

    public static final List<VoteTo> votesTo = getTos(votes);

}
