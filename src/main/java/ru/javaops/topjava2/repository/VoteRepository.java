package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.RestaurantC;
import ru.javaops.topjava2.model.Vote;

import java.util.List;
import java.util.Optional;
@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote>{

    @EntityGraph(attributePaths = {"user","restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v ORDER BY v.id ASC")
    List<Vote> getAllWithUserAndRestaurant();

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.id=:id")
    Optional<Vote> getByIdWithUser(Integer id);

    @EntityGraph(attributePaths = {"user", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.id=:id")
    Optional<Vote> getByIdWithRestaurantAndUser(Integer id);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:id AND v.regDate=current_date")
    Optional<Vote> findByUserId(int id);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id=:id")
    List<Vote> findAllByUserId(int id);


    //https://www.baeldung.com/jpa-queries-custom-result-with-aggregation-functions
    @Query("SELECT new ru.javaops.topjava2.model.RestaurantC(v.restaurant, count(v)) FROM Vote v INNER JOIN v.restaurant WHERE v.regDate=current_date GROUP BY v.restaurant")
    List<RestaurantC> getResultVotes();

}
