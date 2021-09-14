package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Question;

import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
public interface QuestionRepository extends JpaRepository<Question,Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Question q WHERE q.id=:id")
    int delete(@Param("id") int id);

    @EntityGraph(attributePaths = {"poll"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Question m WHERE m.id=:id")
    Optional<Question> findByIdWithPoll(Integer id);

    @Query("SELECT m FROM Question m WHERE m.poll.id=:pollId")
    List<Question> findAllByPollId(int pollId);

}
