package ru.bykov.insidetest.payload;

import lombok.Data;

@Data
public class LoginDto {
    private String name;
    private String password;
}
