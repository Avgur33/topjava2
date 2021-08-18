package ru.javaops.topjava2.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.hibernate.validator.constraints.Range;
import ru.javaops.topjava2.HasId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantTo extends NamedTo implements HasId {

    @NotBlank
    @Size(min = 5,max = 100)
    String location;

    @NotNull
    @Range(min = 0, max = Integer.MAX_VALUE)
    Integer votes;

    public RestaurantTo(Integer id, String name, String location, Integer votes) {
        super(id, name);
        this.location = location;
        this.votes = votes;
    }
}
