package ru.bykov.insidetest.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SignUpDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 64)
    private String name;
    @NotNull
    @NotBlank
    @Email
    @Size(min = 1, max = 128)
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 64)
    private String password;
    private List<String> roles;
}
