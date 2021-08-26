package ru.javaops.topjava2.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.RestaurantC;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.to.MenuTo;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javaops.topjava2.util.MenuUtil.getTos;
import static ru.javaops.topjava2.util.validation.ValidationUtil.checkCurrentTime;

@RestController
@RequestMapping(value = RootController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j


public class RootController {
    public final static String REST_URL = "/api/root";

    @Value("${limit-time.vote}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime timeLimit;

    private final MenuRepository menuRepository;
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    public RootController(MenuRepository menuRepository, VoteRepository voteRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<MenuTo>> get(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        log.info("get Menus for today");
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Menu> menuList = menuRepository.getToday(pageable);
        return ResponseEntity.ok(getTos(menuList));
    }

    @Transactional
    @GetMapping("/result")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RestaurantC>> getResult() {
        log.info("get Result for today");

        List<RestaurantC> resultList = voteRepository.getResultVotes();
        return ResponseEntity.ok(resultList);
    }

    //проголосовать
    @PostMapping("/vote")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Vote> createVoteWithLocation(@RequestParam Integer restaurantId, @AuthenticationPrincipal AuthUser user) {
        log.info("Vote");
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()->new NotFoundException("Restaurant with id=" + restaurantId + " not found"));
        checkCurrentTime(timeLimit);
        Vote vote = new Vote(null, LocalDate.now(), user.getUser(), restaurant);
        Vote created = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    //получить голос за сегодня если уже голосовал
    @GetMapping("/vote")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Vote> getVote(@AuthenticationPrincipal AuthUser user) {
        log.info("Vote");
        Vote vote = voteRepository.findByUserId(user.id())
                .orElseThrow(()->new NotFoundException("Vote with user id=" + user.id() + " not found"));
        return ResponseEntity.ok(vote);
    }

    @GetMapping("/vote/history")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Vote>> getAllVotes(@AuthenticationPrincipal AuthUser user) {
        log.info("Vote");
        List <Vote> votes = voteRepository.findAllByUserId(user.id());
        return ResponseEntity.ok(votes);
    }
}
