package ru.bykov.insidetest.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTAuthResponse {
    private String token;
    public JWTAuthResponse(String accessToken) {
        this.token = accessToken;
    }
}
