package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Dish;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT m FROM Dish m WHERE m.restaurant.id =:restaurantId AND m.forDate=current_date ORDER BY m.price ASC")
    List<Dish> getDishesByRestaurantId(@Param("restaurantId") Integer restaurantId);

    @Query("SELECT m FROM Dish m WHERE m.restaurant.id =:restaurantId AND m.forDate >= :startDate AND m.forDate <= :endDate ORDER BY m.forDate ASC ")
    List<Dish> getHistory(@Param("restaurantId")Integer restaurantId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT m FROM Dish m JOIN FETCH m.restaurant WHERE m.id =:id ")
    Optional<Dish> findByIdWithRestaurant(Integer id);
}
