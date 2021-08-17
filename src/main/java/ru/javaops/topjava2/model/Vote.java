package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
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
    @JsonBackReference
    private Restaurant restaurant;
}
