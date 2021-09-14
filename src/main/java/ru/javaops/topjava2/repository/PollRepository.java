package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Poll;

import java.util.List;

@Transactional(readOnly = true)
public interface PollRepository extends JpaRepository<Poll,Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Poll p WHERE p.id=:id")
    int delete(@Param("id") int id);



    @Query("SELECT p FROM Poll p WHERE p.startDate >= current_date AND p.endDate <= current_date ")
    List<Poll> getActive();
}
