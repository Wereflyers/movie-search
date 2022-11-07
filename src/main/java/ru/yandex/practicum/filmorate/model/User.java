package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^\\S*$")
    private String login;
    @NotBlank
    @NotNull
    private String name;
    private LocalDate birthday;
}
