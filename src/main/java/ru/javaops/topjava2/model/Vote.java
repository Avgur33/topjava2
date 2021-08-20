package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name="vote", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"reg_date", "user_id"}, name = "vote_unique_reg_date_user_id_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Vote extends BaseEntity {

    @Column(name = "reg_date", nullable = false,columnDefinition = "date default now()")
    @NotNull
    private LocalDate regDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference(value = "restaurant-vote")
    private Restaurant restaurant;

    public Vote(Integer id, LocalDate regDate, User user, Restaurant restaurant) {
        super(id);
        this.regDate = regDate;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Vote(int id, LocalDate regDate) {
        super(id);
        this.regDate = regDate;
    }
}
