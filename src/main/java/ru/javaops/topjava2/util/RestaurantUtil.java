package ru.javaops.topjava2.util;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class RestaurantUtil {
    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .map(r -> createTo(r, (int) r
                        .getVotes()
                        .stream()
                        .filter(v -> v.getRegDate().compareTo(LocalDate.now()) == 0)
                        .count()))
                .toList();
    }
        /*        return restaurants.stream()
                .map(r->createToWith(r, r
                        .getVotes()
                        .stream()
                        .filter(v->v.getRegDate().compareTo(LocalDate.now()) == 0)
                        .collect(Collectors.groupingBy(Vote::getRegDate, Collectors.counting()))))
                .toList();*/


/*    public static List<RestaurantTo> getTosHistory(Collection<Restaurant> restaurants,LocalDate startDate, LocalDate endDate){
        return restaurants.stream()
                .map(r->createToWith(r, r
                        .getVotes()
                        .stream()
                        .filter(v->v.getRegDate().compareTo(startDate) > -1 && v.getRegDate().compareTo(endDate) < 1 )
                        .collect(Collectors.groupingBy(Vote::getRegDate, Collectors.counting()))))
                .toList();
    }*/

/*    private static RestaurantTo createToWith(Restaurant rest, Map<LocalDate, Long> collect) {
        return new RestaurantTo(rest.getId(),rest.getName(), rest.getLocation(), 0,collect);
    }*/

    public static RestaurantTo createTo(Restaurant rest, int votes) {
        return new RestaurantTo(rest.getId(),rest.getName(), rest.getLocation(), votes);
    }

    public static RestaurantTo createITo(Integer restaurantId, String name, String location, int intValue) {
        return new RestaurantTo(restaurantId,name, location, intValue);
    }
}