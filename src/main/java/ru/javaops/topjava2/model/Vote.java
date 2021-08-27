package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="vote", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"reg_date", "user_id"}, name = "vote_unique_reg_date_user_id_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Vote extends BaseEntity {

    @Column(name = "reg_date", nullable = false,columnDefinition = "date default now()")
    @NotNull
    @Schema(description = "Registration date", example = "2020-02-20",format = "yyyy-MM-dd")
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate regDate;

    @Column(name = "reg_time", nullable = false,columnDefinition = "time default now()")
    @NotNull
    @Schema(description = "Registration time", example = "11:30:20",format = "HH:mm:ss")
    @DateTimeFormat(iso=DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime regTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    @Hidden
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference(value = "restaurant-vote")
    @Hidden
    private Restaurant restaurant;

    public Vote(Integer id, LocalDate regDate, User user, Restaurant restaurant) {
        super(id);
        this.regDate = regDate;
        this.regTime = LocalTime.now();
        this.user = user;
        this.restaurant = restaurant;
    }
    public Vote( Vote v){
        this(v.getId(), v.getRegDate(),v.getUser(),v.getRestaurant());
    }

}
