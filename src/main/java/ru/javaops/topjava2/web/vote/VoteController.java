package ru.javaops.topjava2.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.web.AuthUser;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.VoteUtil.createTo;
import static ru.javaops.topjava2.util.VoteUtil.getTos;


@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {
    public final static String REST_URL = "/api/votes";
    private final VoteRepository repository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Vote delete {}", id);
        repository.deleteExisted(id);
    }

    @PostMapping(value = "")
    public ResponseEntity<Vote> createWithLocation(@RequestParam @NotNull Integer restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("create vote for restaurant {}", restaurantId);
        Restaurant rest = new Restaurant();
        rest.setId(restaurantId);
        Vote vote = new Vote(null, LocalDate.now(), authUser.getUser(), rest);
        Vote created = repository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VoteTo> get(@PathVariable @NotNull @Min(1) Integer id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("get vote {}", id);
        //ToDo проверка на NULL
        return ResponseEntity.ok(createTo(repository.getByIdWithUserAndRestaurant(id)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestParam Integer restaurant_id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update with id={}", id);
        //ToDo проверка на валидность юзера, голос принадлежит именно этому юзеру
        Vote updated = repository.getById(id);
        updated.setRestaurant(new Restaurant(restaurant_id));
    }

    @Operation(
            summary = "get all votes",
            description = "get all votes with user name and restaurant name"
    )
    @GetMapping
    public List<VoteTo> getAll() {
        log.info("Vote getAll");
        return getTos(repository.getAllWithUserAndRestaurant());
    }
}
