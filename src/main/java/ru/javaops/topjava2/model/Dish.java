package ru.javaops.topjava2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="dish", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"name","price"}, name = "dish_unique_name_price_idx")})
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

    public Dish(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }
}


