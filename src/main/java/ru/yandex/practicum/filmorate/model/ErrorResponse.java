package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String name;
    private final String message;

    public ErrorResponse(String message) {
        name = "error";
        this.message = message;
    }
}
