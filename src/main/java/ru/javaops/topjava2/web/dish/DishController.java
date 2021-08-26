package ru.javaops.topjava2.web.dish;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.error.ErrorInfo;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.repository.DishRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishController {
    private final DishRepository repository;
    private final RestaurantRepository restaurantRepository;
    public final static String REST_URL = "/api/admin/restaurants/{restaurantId}/dishes";

    /*@Value("${limit-time.dish}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime timeLimit;*/

/*    @Operation(
            summary = "Get all dishes for restaurant",
            description = "get dishes between startDate and endDate",
            parameters = {
                    @Parameter(name = "restaurantId",
                            description = "The id of restaurant. Use 1 for testing.",
                            content = @Content(examples = {@ExampleObject(value = "1")}),
                            required = true),
                    @Parameter(name = "startDate",
                            description = "Start date. Format yyyy-MM-dd.",
                            content = @Content(examples = {@ExampleObject(value = "2020-02-21")}),
                            required = false),
                    @Parameter(name = "endDate",
                            description = "End date. Format yyyy-MM-dd.",
                            content = @Content(examples = {@ExampleObject(value = "2022-02-21")}),
                            required = false)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dishes for the restaurant",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Dish.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorInfo.class)))
            }
    )
    @GetMapping(value = "/history")
    public List<Dish> getHistory(
            @PathVariable Integer restaurantId,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return null;
    }*/

    @Operation(
            summary = "Delete dish with ID",
            description = "Only for Admin",
            parameters = {
                    @Parameter(name = "restaurantId",
                            description = "The id of restaurant. Use 1 for testing.",
                            content = @Content(examples = {@ExampleObject(value = "1")}),
                            required = true),
                    @Parameter(name = "id",
                            description = "The id of dish that needs to be deleted. Use 1 for testing.",
                            content = @Content(examples = {@ExampleObject(value = "1")}),
                            required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "The dish was deleted", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorInfo.class)))
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer restaurantId, @PathVariable Integer id) {

        log.info("Restaurant delete {}", id);
        Dish dish = repository
                .findByIdWithRestaurant(id)
                .orElseThrow(() -> new NotFoundException(" Entity Dish with id = " + id + " not found"));
        assureIdConsistent(dish.getRestaurant(), restaurantId);
        repository.deleteExisted(id);
    }

    @Operation(
            summary = "Get dishes for restaurant for the current date",
            parameters = {
                    @Parameter(name = "restaurantId",
                            description = "The id of restaurant. Use 1 for testing.",
                            content = @Content(examples = {@ExampleObject(value = "1")}),
                            required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "The dishes",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Dish.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            }
    )
    @GetMapping()
    public List<Dish> getAll(@PathVariable int restaurantId) {
        //ToDo валидация Id ресторана (такого ресторана нет)

        log.info("get dishes for restaurant {}", restaurantId);
        return repository.getDishesByRestaurantId(restaurantId);
    }

    @Operation(
            summary = "Get dish for the restaurant(restaurantId) by id",
            parameters = {
                    @Parameter(name = "restaurantId",
                            description = "The id of restaurant. Use 1 for testing.",
                            content = @Content(examples = {@ExampleObject(value = "1")}),
                            required = true),
                    @Parameter(name = "id",
                            description = "The id of dish that needs to be fetched. Use 1 for testing.",
                            content = @Content(examples = {@ExampleObject(value = "1")}),
                            required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dish for the restaurant",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Dish.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorInfo.class))),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorInfo.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable Integer restaurantId, @PathVariable Integer id) {
        log.info("get dish by ID for restaurant {}", restaurantId);
        Dish dish = repository
                .findByIdWithRestaurant(id)
                .orElseThrow(() -> new NotFoundException(" Entity Dish with id = " + id + " not found"));
        assureIdConsistent(dish.getRestaurant(), restaurantId);
        return ResponseEntity.ok(dish);
    }

    @Operation(
            summary = "Create dish for restaurant for today",
            description = "Only for Admin",
            parameters = {
                    @Parameter(name = "restaurantId",
                            description = "The id of restaurant. Use 1 for testing.",
                            content = @Content(examples = {@ExampleObject(value = "1")}),
                            required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created dish for the restaurant",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Dish.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorInfo.class))),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorInfo.class))),
                    @ApiResponse(responseCode = "423", description = "Locked",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorInfo.class)))
            }
    )

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Dish> creatWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Dish dish) {
        log.info("create {}", dish);
        checkNew(dish);
        dish.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new NotFoundException("Restaurant with id=" + restaurantId + " not found")));
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(
            summary = "Update dish",
            description = "Only for Admin",
            parameters = {
                    @Parameter(name = "restaurantId",
                            description = "The id of restaurant. Use 1 for testing.",
                            content = @Content(examples = {@ExampleObject(value = "1")}),
                            required = true),
                    @Parameter(name = "id",
                            description = "The id of dish that needs to be updated. Use 1 for testing.",
                            content = @Content(examples = {@ExampleObject(value = "1")}),
                            required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Created dish for the restaurant",
                            content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorInfo.class))),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorInfo.class))),
            }
    )

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody Dish dish) {
        log.info("update {} with id={}", dish, id);
        assureIdConsistent(dish, id);
        Dish updated = repository.getById(id);
        assureIdConsistent(updated.getRestaurant(), restaurantId);
        updated.setName(dish.getName());
        updated.setPrice(dish.getPrice());
    }
}
