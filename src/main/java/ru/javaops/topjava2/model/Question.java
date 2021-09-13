package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "text", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String text;


    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("type DESC")
    @JsonManagedReference(value = "question_answer")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    private List<Answer> answers;



}
