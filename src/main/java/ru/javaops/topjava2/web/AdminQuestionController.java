package ru.javaops.topjava2.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.Question;
import ru.javaops.topjava2.repository.PollRepository;
import ru.javaops.topjava2.repository.QuestionRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNew;

@Slf4j
@RestController
@RequestMapping(value = AdminQuestionController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminQuestionController {
    public static final String REST_URL = "/api/admin/poll/{pollId}/question";
    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private QuestionRepository questionRepository;
    @DeleteMapping("/{id}")


    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int pollId, @PathVariable int id) {
        log.info("Delete question");
        Question question = questionRepository.findByIdWithPoll(id).get();
        assureIdConsistent(question.getPoll(),pollId);
        questionRepository.delete(id);
    }

    @GetMapping
    public List<Question> getAll(@PathVariable int pollId) {
        log.info("getAll");
        return questionRepository.findAllByPollId(pollId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Question get(@PathVariable int pollId, @PathVariable int id) {
        log.info("get");
        Question question = questionRepository.findByIdWithPoll(id).get();
        assureIdConsistent(question.getPoll(),pollId);
        return question;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Question> createWithLocation(@PathVariable int pollId, @Valid @RequestBody Question question) {
        log.info("create {}", question);
        checkNew(question);
        question.setPoll(pollRepository.getById(pollId));
        Question created = questionRepository.save(question);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(pollId,created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int pollId, @PathVariable int id, @Valid @RequestBody Question question) {
        log.info("update {} with id={}", question, id);
        assureIdConsistent(question, id);
        Question updated = questionRepository.findByIdWithPoll(id).get();
        assureIdConsistent(updated.getPoll(), id);
        updated.setText(question.getText());
        updated.setType(question.getType());
        questionRepository.save(question);
    }
}
