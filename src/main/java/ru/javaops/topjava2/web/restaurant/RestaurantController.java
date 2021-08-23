package ru.javaops.topjava2.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.to.RestaurantTo;

import javax.validation.Valid;
import java.net.URI;
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
    public final static String REST_URL = "/api/restaurants";

    @Operation(
            summary = "Get restaurant by restaurant id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The restaurant",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Restaurant.class))),
            @ApiResponse(responseCode = "422", description = "Error"),
            @ApiResponse(responseCode = "401", description = "Error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(
            @Parameter(description = "The id of restaurant that needs to be fetched. Use 1 for testing.", required = true)@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.ok(repository.findById(id).orElseThrow(() ->
                new NotFoundException("Entity with id=" + id + " not found")));
    }

    @Operation(
            summary = "Delete restaurant",
            description = "Only for Admin"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = ""),
            @ApiResponse(responseCode = "422", description = "Error"),
            @ApiResponse(responseCode = "403", description = "Error"),
            @ApiResponse(responseCode = "401", description = "Error")
    })

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable int id) {
        log.info("Restaurant delete {}", id);
        repository.deleteExisted(id);
    }

    @Operation(
            summary = "Create restaurant",
            description = "Only for Admin"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "422", description = "Error"),
            @ApiResponse(responseCode = "403", description = "Error"),
            @ApiResponse(responseCode = "401", description = "Error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Restaurant> creatWithLocation(@RequestBody @Valid Restaurant rest) {
        log.info("create {}", rest);
        checkNew(rest);
        Restaurant created = repository.save(rest);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(
            summary = "Update restaurant",
            description = "Only for Admin"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = ""),
            @ApiResponse(responseCode = "422", description = "Error"),
            @ApiResponse(responseCode = "403", description = "Error"),
            @ApiResponse(responseCode = "401", description = "Error")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void update(@RequestBody @Valid Restaurant rest, @PathVariable int id) {
        log.info("update {} with id={}", rest, id);
        assureIdConsistent(rest, id);
        repository.save(rest);
    }

    @Operation(
            summary = "Get all restaurants with number of votes for today",
            description = ""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "422", description = "Error"),
            @ApiResponse(responseCode = "401", description = "Error")
    })
    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("Restaurant getAll");
        return getTos(repository.getAllWithVotes());
    }

/*    @Operation(
            summary = "получаем рестораны с историей голосований",
            description = ""
    )
    @GetMapping("/history")
    public List<RestaurantTo> getAllHistory(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Restaurant getAllHistory");
        return getTosHistory(repository.getAllWithVotes(),startDateUtil(startDate) , endDateUtil(endDate));
    }*/
}
