package ru.javaops.topjava2.util;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.to.RestaurantTo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestaurantUtil {
    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .map(r->createTo(r, (int) r
                        .getVotes()
                        .stream()
                        .filter(v->v.getRegDate().compareTo(LocalDate.now()) == 0)
                        .count()))
                .toList();
    }

    public static List<RestaurantTo> getTosHistory(Collection<Restaurant> restaurants){
        return restaurants.stream()
                .map(r->createToWith(r, r
                        .getVotes()
                        .stream()
                        .collect(Collectors.groupingBy(Vote::getRegDate, Collectors.counting()))))
                .toList();
    }

    private static RestaurantTo createToWith(Restaurant rest, Map<LocalDate, Long> collect) {
        return new RestaurantTo(rest.getId(),rest.getName(), rest.getLocation(), null,collect);
    }

    public static RestaurantTo createTo(Restaurant rest, int votes) {
        return new RestaurantTo(rest.getId(),rest.getName(), rest.getLocation(), votes);
    }
}