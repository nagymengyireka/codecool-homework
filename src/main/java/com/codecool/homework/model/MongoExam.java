package com.codecool.homework.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "exam")
@NoArgsConstructor
public class MongoExam {

    @Id
    @Getter @Setter
    private String id;

    @Getter @Setter
    private Module module;

    @Getter @Setter
    private String mentor; //email

    @Getter @Setter
    private String student; //email

    @Getter @Setter
    private LocalDate date;

    @Getter @Setter
    private boolean cancelled;

    @Getter @Setter
    private boolean success;

    @Getter @Setter
    private String comment;

    @Getter @Setter
    private List<MongoResult> results;

}
