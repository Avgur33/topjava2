package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Dish;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    @Query("SELECT m FROM Dish m JOIN FETCH m.restaurant WHERE m.restaurant.id=:restaurantId and m.forDate=current_date ORDER BY m.price ASC")
    List<Dish> getAllByRestaurantId(@Param("restaurantId") int restaurantId);

    @Query("SELECT m FROM Dish m JOIN FETCH m.restaurant WHERE m.forDate=current_date ORDER BY m.price ASC")
    List<Dish> getAllCurrentWithRestaurant();

    @Query("SELECT m FROM Dish m JOIN FETCH m.restaurant WHERE m.forDate >= :startDate AND m.forDate < :endDate ORDER BY m.price ASC ")
    List<Dish> getAllFilter(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
