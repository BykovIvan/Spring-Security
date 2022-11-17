package ru.bykov.insidetest.payload;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String accessToken;
}
