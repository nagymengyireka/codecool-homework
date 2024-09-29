package com.codecool.homework.model.entity;

import com.codecool.homework.model.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class CodecoolUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String name;

    @Column(unique = true)
    @Getter @Setter
    private String email;

    @Getter @Setter
    private LocalDate dateOfBirth;

    @Getter @Setter
    private Role role;
}
