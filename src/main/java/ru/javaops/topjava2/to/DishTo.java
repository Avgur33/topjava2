package ru.javaops.topjava2.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.hibernate.validator.constraints.Range;
import ru.javaops.topjava2.HasId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DishTo extends NamedTo implements HasId {

    @NotNull
    @Range(min = 10)
    Integer   price;

    @NotNull
    LocalDate forDate;


    public DishTo(Integer id, String name, Integer price, LocalDate forDate) {
        super(id, name);
        this.price = price;
        this.forDate = forDate;
    }
}
