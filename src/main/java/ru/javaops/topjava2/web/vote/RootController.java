package ru.javaops.topjava2.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava2.error.NotFoundException;
import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Vote;
import ru.javaops.topjava2.repository.MenuRepository;
import ru.javaops.topjava2.repository.RestaurantRepository;
import ru.javaops.topjava2.repository.VoteRepository;
import ru.javaops.topjava2.to.MenuTo;
import ru.javaops.topjava2.to.RestaurantTo;
import ru.javaops.topjava2.to.VoteTo;
import ru.javaops.topjava2.util.RestaurantUtil;
import ru.javaops.topjava2.util.VoteUtil;
import ru.javaops.topjava2.web.AuthUser;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.javaops.topjava2.util.DateUtil.endDateUtil;
import static ru.javaops.topjava2.util.DateUtil.startDateUtil;
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

    @Operation(
            summary = "Get menus and restaurants for today vote",
            parameters = {
                    @Parameter(name = "pageNumber",
                            description = "Page number ",
                            content = @Content(examples = {@ExampleObject(value = "0")}),
                            required = true),
                    @Parameter(name = "pageSize",
                            description = "Page size ",
                            content = @Content(examples = {@ExampleObject(value = "10")}),
                            required = true)
            })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<MenuTo>> get(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        log.info("get Menus for today");
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Menu> menuList = menuRepository.getToday(pageable);
        return ResponseEntity.ok(getTos(menuList));
    }

    @Operation(summary = "Get voting result")
    @GetMapping("/vote/result")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RestaurantTo>> getResult() {
        log.info("get Result for today");
        List<Vote> resultList = voteRepository.getVotesResult();
        Map<Restaurant, Long> map = resultList.stream().collect(Collectors.groupingBy(Vote::getRestaurant, Collectors.counting()));
        return ResponseEntity.ok(map.entrySet().stream().map(e -> RestaurantUtil.createTo(e.getKey(), e.getValue().intValue())).toList());
    }


    @Operation(summary = "Create vote for authenticated user",
            parameters = {
                    @Parameter(name = "restaurantId",
                            description = "The id of restaurant.",
                            content = @Content(examples = {@ExampleObject(value = "1")}),
                            required = true)
            }
    )

    @PostMapping("/vote")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<Vote> createVoteWithLocation(@RequestParam Integer restaurantId, @AuthenticationPrincipal AuthUser user) {
        log.info("Vote");
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id=" + restaurantId + " not found"));
        checkCurrentTime(timeLimit);
        Vote vote = new Vote(null, LocalDate.now(), user.getUser(), restaurant);
        Vote created = voteRepository.save(vote);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "Get today vote for authenticated user")
    @GetMapping("/vote/by")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<VoteTo> getVote(@AuthenticationPrincipal AuthUser user) {
        log.info("Vote");
        Vote vote = voteRepository.findByUserId(user.id())
                .orElseThrow(() -> new NotFoundException("Vote with user id=" + user.id() + " not found"));
        return ResponseEntity.ok(VoteUtil.createTo(vote));
    }

    @Operation(summary = "Get users history of voting",
            parameters = {
                    @Parameter(name = "startDate",
                            description = "Start date. Format yyyy-MM-dd.",
                            content = @Content(examples = {@ExampleObject(value = "2020-02-21")}),
                            required = false),
                    @Parameter(name = "endDate",
                            description = "End date. Format yyyy-MM-dd.",
                            content = @Content(examples = {@ExampleObject(value = "2022-02-21")}),
                            required = false)
            }
    )
    @GetMapping("/vote/user/history")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<VoteTo>> getAllVotes(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal AuthUser user) {
        log.info("Get history vote");
        List<Vote> votes = voteRepository.findAllByUserIdFilter(user.id(), startDateUtil(startDate), endDateUtil(endDate));
        return ResponseEntity.ok(VoteUtil.getTos(votes));
    }

    @Operation(summary = "Update vote for authenticated user",
            description = "if user not voting yet - create vote",
            parameters = {
                    @Parameter(name = "restaurantId",
                            description = "The id of restaurant.",
                            content = @Content(examples = {@ExampleObject(value = "1")}),
                            required = true)
            }
    )

    @PutMapping("/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestParam Integer restaurantId, @AuthenticationPrincipal AuthUser user) {
        log.info("Get history vote");
        checkCurrentTime(timeLimit);
        Optional<Vote> vote = voteRepository.findByUserId(user.id());
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id=" + restaurantId + " not found"));
        if (vote.isPresent()) {
            vote.get().setRestaurant(restaurant);
            vote.get().setRegTime(LocalTime.now());
        } else {
            voteRepository.save(new Vote(null, LocalDate.now(), user.getUser(), restaurant));
        }
    }
}
