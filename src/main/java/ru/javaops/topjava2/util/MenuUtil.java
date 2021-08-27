package ru.javaops.topjava2.util;

import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.to.MenuTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MenuUtil {
    public static List<MenuTo> getTos(Collection<Menu> menus) {
        return menus.stream()
                .map(r -> createTo(r.getRestaurant(),r.getDishes()))
                .toList();
    }
    public static MenuTo createTo(Restaurant rest, Collection<Dish> dishes) {
        return new MenuTo(rest.getId(),rest.getName(), rest.getLocation(),dishes.stream().collect(Collectors.toMap(Dish::getName,Dish::getPrice)));
    }
}