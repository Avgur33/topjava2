package ru.javaops.topjava2.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.Poll;
import ru.javaops.topjava2.repository.PollRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNew;

@Slf4j
@RestController
@RequestMapping(value = AdminPollController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)

public class AdminPollController {
    static final String REST_URL = "/api/admin/poll";

    @Autowired
    private PollRepository pollRepository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        pollRepository.delete(id);
    }

    @GetMapping
    public List<Poll> getAll() {
        log.info("getAll");
        return pollRepository.findAll(Sort.by(Sort.Direction.ASC,"startDate"));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Poll get(@PathVariable int id) {
        log.info("getById");
        return pollRepository.findById(id).get();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Poll> createWithLocation(@Valid @RequestBody Poll poll) {
        log.info("create {}", poll);
        checkNew(poll);
        Poll created = pollRepository.save(poll);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody Poll poll, @PathVariable int id) {
        log.info("update {} with id={}", poll, id);
        assureIdConsistent(poll, id);
        pollRepository.save(poll);
    }
}
