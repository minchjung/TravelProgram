package com.github.homework.program.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleResponse {
    private final boolean success;
    private final String message;
}
