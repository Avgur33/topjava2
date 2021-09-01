package ru.javaops.topjava2.web.vote;

import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.to.MenuTo;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.util.RestaurantUtil;
import ru.javaops.topjava2.util.VoteUtil;
import ru.javaops.topjava2.web.AbstractTestData;
import ru.javaops.topjava2.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static ru.javaops.topjava2.util.MenuUtil.getTos;
import static ru.javaops.topjava2.web.menu.MenuTestData.allMenusForToday;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.rest1;
import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.rest2;
import static ru.javaops.topjava2.web.user.UserTestData.admin;
import static ru.javaops.topjava2.web.user.UserTestData.user1;

public class RootTestData extends AbstractTestData {

    public static final MatcherFactory.Matcher<MenuTo> MATCHERMENUTO = MatcherFactory.usingIgnoringFieldsComparator(MenuTo.class,  "");
    public static final MatcherFactory.Matcher<RestaurantTo> MATCHERRESTAURANTTO = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class,  "");
    public static final MatcherFactory.Matcher<VoteTo> MATCHERVOTETO = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class,  "");
    public static final MatcherFactory.Matcher<Vote> MATCHERVOTE = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "restaurant", "user","regTime");

    public static final RestaurantTo restTo1 = RestaurantUtil.createTo(rest1,1,null);
    public static final RestaurantTo restTo2 = RestaurantUtil.createTo(rest2,1,null);

    public static final RestaurantTo rest1To =
            new RestaurantTo(rest1.getId(), rest1.getName(),rest1.getLocation(), 1,
                    Map.of(LocalDate.now().minusDays(1),1L, LocalDate.now(), 1L));
    public static final RestaurantTo rest2To =
            new RestaurantTo(rest2.getId(), rest2.getName(),rest2.getLocation(), 1,
                    Map.of(LocalDate.now().minusDays(1),1L, LocalDate.now(), 1L));




    public static final Vote vote1 = new Vote(VOTE1_ID, LocalDate.now().minusDays(1),user1,rest1);
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, LocalDate.now().minusDays(1), admin,rest2);
    public static final Vote vote3 = new Vote(VOTE1_ID + 2, LocalDate.now(),user1,rest1);
    public static final Vote vote4 = new Vote(VOTE1_ID + 3, LocalDate.now(),admin,rest2);

    public static final List<Vote> votes = List.of(vote1, vote2, vote3,vote4);
    public static Vote getUpdated() { return new Vote(VOTE1_ID + 2,LocalDate.now(),null,null );}

    public static final List<VoteTo> votesTo = VoteUtil.getTos(votes);


    public static final List<RestaurantTo> voteResult = List.of(restTo1,restTo2);
    public static final List<RestaurantTo> historyResult = List.of(rest1To,rest2To);

    public static final List<MenuTo> allMenuTosForToday = getTos(allMenusForToday);

}
