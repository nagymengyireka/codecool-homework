package com.codecool.homework.model.entity;

import com.codecool.homework.model.Module;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private Module module;

    @ManyToOne
    @Getter @Setter
    private CodecoolUser mentor;

    @ManyToOne
    @Getter @Setter
    private CodecoolUser student;

    @Getter @Setter
    private LocalDate date;

    @Getter @Setter
    private boolean cancelled;

    @Getter @Setter
    private boolean success;

    @Getter @Setter
    private String comment;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter @Setter
    private List<ExamResult> results;
}
