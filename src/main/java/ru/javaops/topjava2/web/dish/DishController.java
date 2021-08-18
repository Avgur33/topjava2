package ru.javaops.topjava2.web.dish;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.DishRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.to.DishTo;
import ru.javaops.topjava2.web.Views;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.DishUtil.*;
import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishController {
    private final DishRepository repository;
    private final RestaurantRepository restaurantRepository;
    public final static String REST_URL = "/api/restaurants/{restaurantId}/dishes";

    @Operation(
            summary = "получить список еды для ресторана историю, поставить валидацию времени",
            description = "get all dishes with restaurant between startDate and endDate"
    )
    @GetMapping(value = "/history")
    public List<DishTo> getHistory(
            @PathVariable @NotNull Integer restaurantId,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return getTos(repository.getHistory(restaurantId, startDate != null ? startDate : DATE_MIN, endDate != null ? endDate : DATE_MAX));
    }


    @Operation(
            summary = "delete by id",
            description = ""
    )
    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable int id) {
        log.info("Restaurant delete {}", id);
        //ToDo админ не может удалить еду после 11 часов
        repository.deleteExisted(id);
    }

    @Operation(
            summary = "получить список еды для ресторана",
            description = "получаем список еды для ресторана на текущую дату"
    )
    @JsonView(Views.Public.class)
    @GetMapping()
    public List<Dish> getDishes(@PathVariable int restaurantId) {
        log.info("get dishes for restaurant {}", restaurantId);
        return repository.getDishesByRestaurantId(restaurantId);
    }

    @Operation(
            summary = "создаем еду для ресторана, нужно подумать про текущую дату",
            description = ""
    )
    @Transactional
    @JsonView(Views.Public.class)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Dish> creatWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Dish dish) {
        //ToDo админ не может создать еду после 11 часов на текущую дату
        log.info("create {}", dish);
        checkNew(dish);
        dish.setForDate(LocalDate.now());
        dish.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow());
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }


    @Operation(
            summary = "обновляем еду",
            description = ""
    )
    @JsonView(Views.Public.class)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update {} with id={}", dish, id);
        //ToDo админ не может обновить еду после 11 часов на дату старые даты
        assureIdConsistent(dish, id);
        repository.save(dish);
    }
}
