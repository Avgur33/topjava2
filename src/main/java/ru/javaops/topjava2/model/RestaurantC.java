package ru.javaops.topjava2.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantC {
    private Restaurant restaurant;
    private Long votesCount;

    public RestaurantC(Restaurant restaurant, Long votesCount) {
        this.restaurant = restaurant;
        this.votesCount = votesCount;
    }
}
