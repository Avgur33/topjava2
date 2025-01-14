package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
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


    //https://www.baeldung.com/jpa-queries-custom-result-with-aggregation-functions
    /*@Query("SELECT new ru.javaops.topjava2.model.tst(v.restaurant, count(v)) FROM Vote v JOIN v.restaurant WHERE v.regDate=current_date GROUP BY v.restaurant.id")
    List<tst> getTodayVotes();*/

}
