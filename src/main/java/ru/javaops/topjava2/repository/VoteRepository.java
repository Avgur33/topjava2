package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javaops.topjava2.model.Vote;

import java.util.List;

public interface VoteRepository extends BaseRepository<Vote>{

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.restaurant.id=:restaurantId ORDER BY v.id ASC")
    List<Vote> getByRestaurantIdWithUser(@Param("restaurantId") int restaurantId);

    @EntityGraph(attributePaths = {"user","restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v ORDER BY v.id ASC")
    List<Vote> getAllWithUserAndRestaurant();
}
