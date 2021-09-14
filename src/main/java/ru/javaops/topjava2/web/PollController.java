package ru.javaops.topjava2.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.Answer;
import ru.javaops.topjava2.model.Poll;
import ru.javaops.topjava2.model.Question;
import ru.javaops.topjava2.repository.PollRepository;
import ru.javaops.topjava2.repository.QuestionRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = PollController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class PollController {
    public static final String REST_URL = "/api/poll";


    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Poll> getActive() {
        log.info("getActive");
        return pollRepository.getActive();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Question> getQuestions(@PathVariable int id) {
        log.info("getQuestions");
        //return questionRepository.findAllByPollId(id);
        return pollRepository.getPollByIdWithQuestions(id).get().getQuestions();
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void setAnswers(@PathVariable int id, @RequestBody Answer... answers) {
        log.info("getQuestions");
        Poll poll = pollRepository.findById(id).get();
        poll.setAnswers((Arrays.stream(answers).peek(x -> x.setPoll(poll)).toList()));
    }

}
