package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import ru.javaops.topjava2.web.Views;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity

@Table(name = "restaurant", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"name", "location"}, name = "rest_unique_name_location_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Restaurant extends NamedEntity {

    @Column(name = "location", nullable = false)
    @NotBlank
    private String location;

    //@JsonView(Views.Inner.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    @JsonView(Views.Inner.class)
    private List<Vote> votes;

    //@JsonView(Views.Inner.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("price DESC")
    @JsonManagedReference
    @JsonView(Views.Inner.class)
    private List<Dish> dishes;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

}