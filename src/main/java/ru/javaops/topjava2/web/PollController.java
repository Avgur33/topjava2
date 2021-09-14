package ru.javaops.topjava2.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.Poll;
import ru.javaops.topjava2.model.Question;
import ru.javaops.topjava2.repository.PollRepository;
import ru.javaops.topjava2.repository.QuestionRepository;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = PollController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class PollController {
    public static final String REST_URL = "/api/poll";


    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private QuestionRepository  questionRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Poll> getActive() {
        log.info("getActive");
        return pollRepository.getActive();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Question> getQuestions(@PathVariable int id){
        log.info("getQuestions");
        return questionRepository.findAllByPollId(id);
    }

/*    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Question> getQuestions(@PathVariable int id){
        log.info("getQuestions");
        return questionRepository.findAllByPollId(id);
    }*/




}
