package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import ru.javaops.topjava2.web.Views;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"name", "location"}, name = "restaurant_unique_name_location_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "location", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String location;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference(value = "restaurant-vote")
    @ToString.Exclude
    private List<Vote> votes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    //@OrderBy("price DESC")
    @JsonManagedReference(value = "restaurant-dish")
    @ToString.Exclude
    private List<Dish> dishes;

    public Restaurant(Integer id, String name, String location) {
        super(id, name);
        this.location = location;
    }
    public Restaurant( Restaurant r ) {
        this(r.id,r.name, r.location,r.votes,r.dishes);
    }

    public Restaurant(Integer id, String name, String location, List<Vote> votes, List<Dish> dishes) {
        super(id,name);
        this.location = location;
        this.votes = votes;
        this.dishes = dishes;
    }
}