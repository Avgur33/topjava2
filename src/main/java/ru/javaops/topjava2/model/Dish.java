package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import ru.javaops.topjava2.web.Views;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name="dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "for_date"}, name = "dish_unique_name_for_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Dish extends NamedEntity {

    @JsonView(Views.Public.class)
    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 10)
    private Integer price;

    @JsonView(Views.Inner.class)
    @Column(name = "for_date", nullable = false, columnDefinition = "date default now()")
    @NotNull
    private LocalDate forDate;

    @JsonView(Views.Inner.class)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference(value = "restaurant-dish")
    private Restaurant restaurant;

    public Dish(Integer id, String name, Integer price, LocalDate forDate) {
        super(id, name);
        this.price = price;
        this.forDate = forDate;
    }
}


