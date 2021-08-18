package ru.javaops.topjava2.util;

import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.to.DishTo;

import java.util.Collection;
import java.util.List;

public class DishUtil{
        public static List<DishTo> getTos(Collection<Dish> dishes) {
            return dishes.stream().map(DishUtil::createTo).toList();
        }
        public static DishTo createTo(Dish dish) {
            return new DishTo(dish.getId(),dish.getName(), dish.getPrice(), dish.getForDate());
        }
}
