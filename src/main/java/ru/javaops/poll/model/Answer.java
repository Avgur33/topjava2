package ru.javaops.poll.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "answer", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"user_id", "question_id"}, name = "answer_unique_user_question_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Answer extends BaseEntity {

    @Column(name = "user_id")
    @Hidden
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer userId;

    @Column(name = "text", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String text;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @JoinColumn(name = "poll_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonBackReference(value = "poll_answer")
    @Hidden
    @ToString.Exclude
    private Poll poll;

    @JoinColumn(name = "question_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonBackReference(value = "answer_question")
    @Hidden
    @ToString.Exclude
    private Question question;

    public Answer(int userId, String text, Type type, Poll poll, Question question) {
        super(null);
        this.userId = userId;
        this.text = text;
        this.type = type;
        this.poll = poll;
        this.question = question;
    }
}
