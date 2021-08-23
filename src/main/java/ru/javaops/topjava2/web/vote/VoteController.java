package ru.javaops.topjava2.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.web.SecurityUtil;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javaops.topjava2.util.VoteUtil.getTos;
import static ru.javaops.topjava2.util.validation.ValidationUtil.*;


@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteController {
    public VoteController(RestaurantRepository restaurantRepository, VoteRepository repository) {
        this.restaurantRepository = restaurantRepository;
        this.repository = repository;
    }

    //ToDo написать свой конвертер
    @Value("${timeLimit.hour}")
    private int LIMIT_HOUR;

    @Value("${timeLimit.min}")
    private int LIMIT_MIN;

    public final static String REST_URL = "/api/votes";
    private final RestaurantRepository restaurantRepository;
    private final VoteRepository repository;

    @Operation(
            summary = "Get vote",
            description = "For Authenticated user"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(@PathVariable Integer id) {
        log.info("get vote by id={} for Authenticated user", id);
        Vote vote = repository
                .getByIdWithUser(id)
                .orElseThrow(() -> new NotFoundException("Entity vote with id = " + id + "not found"));
        assureIdConsistent(vote.getUser(),SecurityUtil.safeGet().id());
        return ResponseEntity.ok(vote);
    }

    @Operation(
            summary = "Delete vote",
            description = "Only for admin"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable int id) {
        log.info("Vote delete {}", id);
        repository.deleteExisted(id);
    }

    @Operation(
            summary = "Register vote in system for Authenticated user",
            description = "Register vote in system if time before 11:00"
    )
    @PostMapping(value = "")
    public ResponseEntity<Vote> createWithLocation(@RequestParam @NotNull @Min(1) Integer restaurantId) {
        log.info("create vote for restaurant {}", restaurantId);
        checkCurrentTime(LocalTime.of(LIMIT_HOUR,LIMIT_MIN));
        Vote newVote = new Vote(
                null,
                LocalDate.now(),
                SecurityUtil.safeGet().getUser(),
                restaurantRepository
                        .findById(restaurantId)
                        .orElseThrow(() -> new NotFoundException("Entity Restaurant with id= " + restaurantId + " not found")));
        Vote created = repository.save(newVote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(
            summary = "Update vote in system for Authenticated user",
            description = "Update vote in system if time before 11:00"
    )
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestParam Integer restaurantId, @PathVariable Integer id) {
        log.info("update vote for restaurant {}", restaurantId);
        checkCurrentTime(LocalTime.of(LIMIT_HOUR,LIMIT_MIN));
        Vote vote = repository
                .getByIdWithUser(id)
                .orElseThrow(() -> new NotFoundException("Entity Vote with id= " + id + " not found"));
        checkCurrentDate(vote.getRegDate());
        assureIdConsistent(vote.getUser(), SecurityUtil.safeGet().id());
        vote.setRestaurant(restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Entity Restaurant with id= " + restaurantId + " not found")));
    }

    @Operation(
            summary = "Get all votes",
            description = "Get all votes with user name and restaurant name only for Admin"
    )

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<VoteTo> getAll() {
        log.info("Vote getAll");
        return getTos(repository.getAllWithUserAndRestaurant());
    }


    @Operation(
            summary = "Get current vote for Authenticated user",
            description = "Get all votes with user name and restaurant name only for Admin"
    )

    @GetMapping("/history")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<VoteTo> getAllHistory() {
        log.info("Vote getAll");
        return getTos(repository.getAllWithUserAndRestaurant());
    }

}
