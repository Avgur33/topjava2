package ru.javaops.topjava2.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.javaops.topjava2.HasId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VoteTo  extends BaseTo implements HasId {

    @NotNull
    LocalDate regDate;

    @NotBlank
    @Size(min = 2, max = 100)
    String userName;

    @NotBlank
    @Size(min = 2, max = 100)
    String restaurantName;

    public VoteTo(Integer id, LocalDate regDate, String userName, String restaurantName) {
        super(id);
        this.regDate = regDate;
        this.userName = userName;
        this.restaurantName = restaurantName;
    }
}
