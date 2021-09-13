package ru.javaops.topjava2.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "answer")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "text", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String text;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
}
