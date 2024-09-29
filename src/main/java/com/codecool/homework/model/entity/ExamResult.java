package com.codecool.homework.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String dimension;

    @Getter @Setter
    private int result;

}
