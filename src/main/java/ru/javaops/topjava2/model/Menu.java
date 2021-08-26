package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="menu", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"for_date", "restaurant_id"}, name = "menu_unique_for_date_restaurant_id_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Menu extends BaseEntity {

    @Column(name = "for_date", nullable = false, columnDefinition = "date default now()")
    @Schema(description = "For this date", example = "2020-02-20",format = "yyyy-MM-dd")
    @NotNull
    private LocalDate forDate;

    @JoinColumn(name = "restaurant_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference(value = "restaurant-menu")
    @ToString.Exclude
    @Hidden
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @Hidden
    private List<Dish> dishes;

    public Menu(Integer id, LocalDate forDate, Restaurant restaurant, List<Dish> dishes) {
        super(id);
        this.forDate = forDate;
        this.restaurant = restaurant;
        this.dishes = dishes;
    }
}
