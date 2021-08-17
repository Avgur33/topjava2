package ru.javaops.topjava2.web.restaurant;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.web.AuthUser;
import ru.javaops.topjava2.web.Views;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.RestaurantUtil.getTos;
import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantRepository repository;
    private final VoteRepository voteRepository;
    public final static String REST_URL = "/api/restaurants";

    @JsonView(Views.Public.class)
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping("/{id}/dishes")
    public List<Dish> getDishes(@PathVariable int id) {
        log.info("get dishes for restaurant {}", id);
        return repository.getByIdWithDishes(id).orElseThrow().getDishes();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Restaurant delete {}", id);
        repository.deleteExisted(id);
    }

    @JsonView(Views.Public.class)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> creatWithLocation(@Valid @RequestBody Restaurant rest) {
        log.info("create {}", rest);
        checkNew(rest);
        Restaurant created = repository.save(rest);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @JsonView(Views.Public.class)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant rest, @PathVariable int id) {
        log.info("update {} with id={}", rest, id);
        assureIdConsistent(rest, id);
        repository.save(rest);
    }

    @Operation(
            summary = "get all restaurants",
            description = "get restaurants with count votes per current day"
    )
    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("Restaurant getAll");
        return getTos(repository.getAllWithVotes());
    }
}
