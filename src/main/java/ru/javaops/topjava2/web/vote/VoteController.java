package ru.javaops.topjava2.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.web.AuthUser;
import ru.javaops.topjava2.web.SecurityUtil;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static ru.javaops.topjava2.util.VoteUtil.getTos;


@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {

    public final static String REST_URL = "/api/restaurants/{restaurantId}/votes";
    private static final LocalTime EXPIRED_TIME = LocalTime.of(11, 0);
    private final VoteRepository repository;

    @Operation(
            summary = "delete vote in system",
            description = "only for admin"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("Vote delete {}", id);
        //ToDo вставить проверку на принадлежность ресторану, посмотреть наобщий метод delete
        repository.deleteExisted(id);
    }

    @Operation(
            summary = "register vote in system",
            description = "register vote in system if time before 11:00"
    )
    @PostMapping(value = "")
    public void create(@RequestParam @NotNull Integer restaurantId) {
        log.info("create vote for restaurant {}", restaurantId);
        //ToDo обработать ошибку запроса на создание
        /*
        if (LocalTime.now().isBefore(EXPIRED_TIME)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        */
        ;
        Optional<Vote> voteOptional = repository.getTodayUserVote(SecurityUtil.safeGet().getUser().getId());
        if (voteOptional.isPresent()) {
            voteOptional.get().setRestaurant(new Restaurant(restaurantId));
            repository.save(voteOptional.get());
        } else {
            repository.save(new Vote(null, LocalDate.now(), SecurityUtil.safeGet().getUser(), new Restaurant(restaurantId)));
        }

        /*URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);*/
    }

    @Operation(
            summary = "register vote in system",
            description = "register vote in system if time before 11:00"
    )
    @PutMapping(value = "")
    public void update(@RequestParam @NotNull Integer restaurantId) {
        log.info("update vote for restaurant {}", restaurantId);
        //ToDo обработать ошибку запроса на создание

        Optional<Vote> voteOptional = repository.getTodayUserVote(SecurityUtil.safeGet().getUser().getId());
        if (voteOptional.isPresent()) {
            voteOptional.get().setRestaurant(new Restaurant(restaurantId));
            repository.save(voteOptional.get());
        } else {
            repository.save(new Vote(null, LocalDate.now(), SecurityUtil.safeGet().getUser(), new Restaurant(restaurantId)));
        }

    }

    /*@GetMapping(value = "/{id}")
    public ResponseEntity<VoteTo> get(@PathVariable @NotNull @Min(1) Integer id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("get vote {}", id);
        return ResponseEntity.ok(createTo(repository.getByIdWithUserAndRestaurant(id)));
    }*/

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
    //ToDo добавить фильтры
    @GetMapping
    public List<VoteTo> getAll() {
        log.info("Vote getAll");
        return getTos(repository.getAllWithUserAndRestaurant());
    }
}
