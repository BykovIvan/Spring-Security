package ru.bykov.insidetest.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MessageDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 1024)
    private String name;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 1024)
    private String message;
}
