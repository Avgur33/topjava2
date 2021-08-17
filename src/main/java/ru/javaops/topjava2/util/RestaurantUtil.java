package ru.javaops.topjava2.util;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.RestaurantTo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class RestaurantUtil {
    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .map(r->createTo(r, (int) r.getVotes().stream().filter(v->v.getRegDate().compareTo(LocalDate.now()) == 0).count())).toList();
    }
    public static RestaurantTo createTo(Restaurant rest, int votes) {
        return new RestaurantTo(rest.getId(),rest.getName(), rest.getLocation(), votes);
    }
}