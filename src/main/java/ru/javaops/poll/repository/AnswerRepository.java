package ru.javaops.poll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.poll.model.Answer;

import java.util.List;

@Transactional(readOnly = true)
public interface AnswerRepository extends JpaRepository<Answer,Integer> {

    @Query("SELECT a FROM Answer a JOIN FETCH a.poll WHERE a.userId =:id")
    List<Answer> getAnswersWithPollByUserId(int id);
}
