package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="dish", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"name","price","restaurant_id"}, name = "dish_unique_name_restaurant_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 10)
    @Schema(description = "min = 10", example = "100")
    private Integer price;

    @JoinColumn(name = "restaurant_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference(value = "restaurant-dish")
    @ToString.Exclude
    @Hidden
    //@NotNull
    private Restaurant restaurant;

    //https://stackoverflow.com/questions/15155587/hibernate-bidirectional-manytomany-delete-issue
    /*@ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "menu_dishes",
            joinColumns = @JoinColumn(name = "dishes_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"))
    @JsonBackReference(value = "menu-dish")*/
    @ManyToMany(mappedBy = "dishes")
    @ToString.Exclude
    @Hidden
    private List<Menu> menus;



    public Dish(Dish d) {
        this(d.getId(),d.getName(),d.getPrice(),d.getRestaurant());
    }

    public Dish(Integer id, String name, Integer price, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
    }
}


