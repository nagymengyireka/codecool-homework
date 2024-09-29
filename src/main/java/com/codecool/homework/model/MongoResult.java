package com.codecool.homework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class MongoResult {

    @Getter @Setter
    private String dimension;

    @Getter @Setter
    private int result;

}
