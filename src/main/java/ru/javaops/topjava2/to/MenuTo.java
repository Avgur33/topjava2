package ru.javaops.topjava2.to;


import lombok.Value;

import java.util.Map;

@Value
public class MenuTo {

    Integer id;
    String Name;
    String Location;
    Map<String,Integer> dishes;

    public MenuTo(Integer id, String name, String location, Map<String, Integer> dishes) {
        this.id = id;
        Name = name;
        Location = location;
        this.dishes = dishes;
    }
}
