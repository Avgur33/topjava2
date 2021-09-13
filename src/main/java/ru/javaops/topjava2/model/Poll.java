package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "poll")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "startDate", nullable = false)
    @NotNull
    private LocalDate startDate;

    @NotNull
    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "poll")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("type DESC")
    @JsonManagedReference(value = "poll_question")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    private List<Question> questions;

}
