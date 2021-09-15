package ru.javaops.poll.web;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaops.poll.model.Answer;
import ru.javaops.poll.model.Poll;
import ru.javaops.poll.model.Question;
import ru.javaops.poll.repository.AnswerRepository;
import ru.javaops.poll.repository.PollRepository;
import ru.javaops.poll.repository.QuestionRepository;
import ru.javaops.poll.to.AnswerTo;
import ru.javaops.poll.to.PollTo;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = PollController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class PollController {
    public static final String REST_URL = "/api/poll";

    private final PollRepository pollRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public PollController(PollRepository pollRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.pollRepository = pollRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Operation(summary = "Get active polls")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Poll> getActive() {
        log.info("getActive");
        return pollRepository.getActive();
    }

    @Operation(summary = "Get poll by id with questions")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Question> getQuestions(@PathVariable int id) {
        log.info("getQuestions");
        return pollRepository.getPollByIdWithQuestions(id).get().getQuestions();
    }

    @Operation(summary = "Set answers")
    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void setAnswers(@PathVariable int id, @RequestParam int userId, @RequestBody AnswerTo... answerTos) {
        log.info("getQuestions");
        Poll poll = pollRepository.findById(id).get();
        poll.setAnswers((Arrays.stream(answerTos)
                .map(a -> new Answer(userId, a.getText(), a.getType(), poll, questionRepository.findById(a.getQuestionId()).get())).toList()));
    }

    @Operation(summary = "Get history by userId")
    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<PollTo> getHistory(@PathVariable int id) {
        Map<Poll, List<Answer>> history = answerRepository.getAnswersWithPollByUserId(id).stream()
                .collect(Collectors.groupingBy(Answer::getPoll));

        return history.entrySet().stream().map(e -> createTO(e.getKey(), e.getValue())).toList();
    }

    private PollTo createTO(Poll poll, List<Answer> answers) {
        return new PollTo(poll.getName(), poll.getStartDate(), poll.getEndDate(), poll.getDescription(), answers);
    }

}
