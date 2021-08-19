package ru.javaops.topjava2.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.web.SecurityUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.VoteUtil.getTos;
import static ru.javaops.topjava2.util.validation.ValidationUtil.*;


@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {

    public final static String REST_URL = "/api/votes";
    private final RestaurantRepository restaurantRepository;
    private final VoteRepository repository;

    @Operation(
            summary = "delete vote in system",
            description = "only for admin"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable int id) {
        log.info("Vote delete {}", id);
        repository.deleteExisted(id);
    }

    @Operation(
            summary = "register vote in system",
            description = "register vote in system if time before 11:00"
    )
    @PostMapping(value = "")
    public void create(@RequestBody Integer restaurantId) {
        log.info("create vote for restaurant {}", restaurantId);
        checkCurrentTime();
        Vote newVote = new Vote(
                null,
                LocalDate.now(),
                SecurityUtil.safeGet().getUser(),
                restaurantRepository
                        .findById(restaurantId)
                        .orElseThrow(() ->new NotFoundException("Entity Restaurant with id=" + restaurantId + " not found")));
        repository.save(newVote);
    }

    @Operation(
            summary = "update vote in system",
            description = "update vote in system if time before 11:00"
    )

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestParam Integer restaurantId, @PathVariable Integer id) {
        log.info("update vote for restaurant {}", restaurantId);
        //checkCurrentTime();
        Vote vote = repository.getById(id);
        checkCurrentDate(vote.getRegDate());
        //ToDo добавить метод для проверки валидности юзера с нормальным выводом сообщения об ошибке
        assureIdConsistent(vote.getUser(),SecurityUtil.safeGet().getUser().getId());
        vote.setRestaurant(restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() ->new NotFoundException("Entity Restaurant with id=" + restaurantId + " not found")));
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
