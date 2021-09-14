package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "poll", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"name", "start_date", "end_date"}, name = "poll_unique_name_startDate_endDate_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Poll extends BaseEntity{

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false,updatable = false)
    @NotNull
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "poll")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("type DESC")
    @JsonManagedReference(value = "poll_question")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Hidden
    private List<Question> questions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "poll",cascade = CascadeType.ALL)//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference(value = "poll_answer")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Hidden
    private List<Answer> answers;

}
