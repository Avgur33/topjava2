package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import ru.javaops.topjava2.web.Views;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"name", "location"}, name = "rest_unique_name_location_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity implements Serializable {

    @JsonView(Views.Public.class)
    @Column(name = "location", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String location;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference
    @JsonView(Views.Inner.class)
    @ToString.Exclude
    private List<Vote> votes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    //@OrderBy("price DESC")
    @JsonManagedReference
    @JsonView(Views.Inner.class)
    @ToString.Exclude
    private List<Dish> dishes;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }
    public Restaurant(Integer id ) {
        super(id, null);
    }

}