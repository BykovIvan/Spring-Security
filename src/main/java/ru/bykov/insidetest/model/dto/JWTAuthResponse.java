package ru.bykov.insidetest.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JWTAuthResponse {
    private String token;

    @JsonCreator
    JWTAuthResponse(@JsonProperty("token") String token) {
        this.token = token;
    }
}
