package ru.javaops.topjava2.web.menu;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Dish;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.repository.DishRepository;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaops.topjava2.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkCurrentDate;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class MenuController {
    public final static String REST_URL = "/api/admin/restaurants/{restaurantId}/menu";

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;


    @PostMapping()
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Menu> creatWithLocation(@PathVariable int restaurantId, @RequestParam List<Integer> dishes) {
        log.info("create menu for the Restaurant id = {}", restaurantId);
        if ((dishes.size() < 2) || (dishes.size() > 5)) {
            throw new NotFoundException("Wrong dishes number");
        }
        Restaurant rest = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(()->new NotFoundException("Restaurant with id=" + restaurantId + " not found"));

        List<Dish> dishList = dishes.stream()
                .map(id->dishRepository.findById(id).orElseThrow(()->new NotFoundException("Dish with id=" + id + " not found")))
                .toList();

        Menu menu = new Menu(null, LocalDate.now(),rest,dishList);

        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer restaurantId, @PathVariable Integer id) {

        log.info("Menu delete {}", id);
        Menu menu = menuRepository
                        .findByIdWithRestaurant(id)
                        .orElseThrow(() -> new NotFoundException(" Entity Menu with id = " + id + " not found"));
        assureIdConsistent(menu.getRestaurant(), restaurantId);
        menuRepository.deleteExisted(id);
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Menu> get(@PathVariable Integer restaurantId, @PathVariable Integer id) {
        log.info("get Menu by ID for restaurant {}", restaurantId);
        Menu menu = menuRepository
                .findByIdWithRestaurant(id)
                .orElseThrow(() -> new NotFoundException(" Entity Menu with id = " + id + " not found"));
        assureIdConsistent(menu.getRestaurant(), restaurantId);
        return ResponseEntity.ok(menu);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Menu>> getAll(@PathVariable Integer restaurantId) {
        log.info("get Menu by ID for restaurant {}", restaurantId);
        List<Menu> menus = menuRepository.findAllByRestaurant(restaurantId);
        return ResponseEntity.ok(menus);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int restaurantId, @PathVariable int id, @RequestParam List<Integer> dishes){
        log.info("update Menu with id={}", id);
        if ((dishes.size() < 2) || (dishes.size() > 5)) {
            throw new NotFoundException("Wrong dishes number");
        }
        Menu menu = menuRepository.getById(id);
        checkCurrentDate(menu.getForDate());
        assureIdConsistent(menu.getRestaurant(), restaurantId);
        List<Dish> dishList = dishes.stream()
                .map(i->dishRepository.findById(i).orElseThrow(()->new NotFoundException("Dish with id=" + i + " not found")))
                .toList();

        menu.setDishes(dishList);
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void addDishes(@PathVariable int restaurantId, @PathVariable int id, @RequestParam List<Integer> dishes){
        log.info("patch Menu with id={}", id);
        Menu menu = menuRepository.getById(id);
        checkCurrentDate(menu.getForDate());
        assureIdConsistent(menu.getRestaurant(), restaurantId);
        if ((menu.getDishes().size() + dishes.size()) > 5){ throw new NotFoundException("Too many dishes");}

        List<Dish> dishList = dishes.stream()
                .map(i->dishRepository.findById(i).orElseThrow(()->new NotFoundException("Dish with id=" + i + " not found")))
                .toList();
        menu.getDishes().addAll(dishList);
    }
}
