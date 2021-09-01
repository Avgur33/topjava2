package ru.javaops.topjava2.web.restaurant;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.web.AbstractTestData;
import ru.javaops.topjava2.web.MatcherFactory;

import java.util.List;

public class RestaurantTestData extends AbstractTestData {

    public static final MatcherFactory.Matcher<Restaurant> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "votes", "dishes");
    public static final MatcherFactory.Matcher<RestaurantTo> MATCHERTO = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class);

    public static final Restaurant rest1 = new Restaurant(REST1_ID, "Burger King","Moscow");
    public static final Restaurant rest2 = new Restaurant(REST1_ID + 1, "Pho Bo","Moscow");
    public static final Restaurant rest3 = new Restaurant(REST1_ID + 2, "KFC","Moscow");

    public static final RestaurantTo rest1To =
            new RestaurantTo(rest1.getId(), rest1.getName(),rest1.getLocation(), 1,null);
    public static final RestaurantTo rest2To =
            new RestaurantTo(rest2.getId(), rest2.getName(),rest2.getLocation(), 1,null);
    public static final RestaurantTo rest3To =
            new RestaurantTo(rest3.getId(), rest3.getName(),rest3.getLocation(), 0,null);


    public static final List<Restaurant> restaurants = List.of(rest1, rest2, rest3);
    public static final List<RestaurantTo> restaurantsTo = List.of(rest1To, rest2To, rest3To);

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant","Moscow");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(REST1_ID, "Updated Restaurant","Moscow");
    }
}
