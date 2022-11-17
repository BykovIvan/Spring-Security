package ru.bykov.insidetest.service;

import org.springframework.http.ResponseEntity;
import ru.bykov.insidetest.payload.JWTAuthResponse;
import ru.bykov.insidetest.payload.LoginDto;
import ru.bykov.insidetest.payload.SignUpDto;

public interface UserService {

    ResponseEntity<?> createUser(SignUpDto signUpDto);

    ResponseEntity<JWTAuthResponse> authenticateUser(LoginDto loginDto);
}
