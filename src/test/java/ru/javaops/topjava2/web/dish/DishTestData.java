package ru.javaops.topjava2.web.dish;

import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.web.AbstractTestData;
import ru.javaops.topjava2.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.web.restaurant.RestaurantTestData.rest1;

public class DishTestData extends AbstractTestData {

    public static final MatcherFactory.Matcher<Dish> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class,  "restaurant");

    public static final Dish dish1 = new Dish(DISH1_ID + 0, "burger 1", 100, null);
    public static final Dish dish2 = new Dish(DISH1_ID + 1, "burger 2", 200, null);
    public static final Dish dish3 = new Dish(DISH1_ID + 2, "burger 3", 300, null);
    public static final Dish dish4 = new Dish(DISH1_ID + 3, "pho bo 1", 400, null);
    public static final Dish dish5 = new Dish(DISH1_ID + 4, "pho bo 2", 500, null);
    public static final Dish dish6 = new Dish(DISH1_ID + 5, "pho bo 3", 600, null);
    public static final Dish dish7 = new Dish(DISH1_ID + 6, "chicken 1", 700, null);
    public static final Dish dish8 = new Dish(DISH1_ID + 7, "chicken 2", 800, null);
    public static final Dish dish9 = new Dish(DISH1_ID + 8, "chicken 3", 900, null);
    /*public static final Dish dish10 = new Dish(DISH10_ID + 0, "burger 1", 100, LocalDate.now());
    public static final Dish dish11 = new Dish(DISH1_ID + 10, "burger 2", 200, LocalDate.now());
    public static final Dish dish12 = new Dish(DISH1_ID + 11, "burger 3", 300, LocalDate.now());
    public static final Dish dish13 = new Dish(DISH1_ID + 12, "pho bo 1", 400, LocalDate.now());
    public static final Dish dish14 = new Dish(DISH1_ID + 13, "pho bo 2", 500, LocalDate.now());
    public static final Dish dish15 = new Dish(DISH1_ID + 14, "pho bo 3", 600, LocalDate.now());
    public static final Dish dish16 = new Dish(DISH1_ID + 15, "chicken 1", 700, LocalDate.now());
    public static final Dish dish17 = new Dish(DISH1_ID + 16, "chicken 2", 800, LocalDate.now());
    public static final Dish dish18 = new Dish(DISH1_ID + 17, "chicken 3", 900, LocalDate.now())*/;

    public static final List<Dish> allDishesOfRestaurant1 = List.of(dish1, dish2, dish3);

    public static Dish getNew() {
        return new Dish(null, "New Dish", 1000, null);
    }
    public static Dish getUpdated(){return new Dish(DISH1_ID, "Updated Dish", 1000, null);}
}
