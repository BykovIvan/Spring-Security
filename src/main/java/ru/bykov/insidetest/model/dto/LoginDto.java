package ru.bykov.insidetest.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
    @NotNull
    @Size(min = 1, max = 64)
    private String name;
    @NotNull
    @Size(min = 1, max = 64)
    private String password;
}
