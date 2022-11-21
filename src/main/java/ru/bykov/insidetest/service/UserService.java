package ru.bykov.insidetest.service;

import org.springframework.http.ResponseEntity;
import ru.bykov.insidetest.model.dto.JWTAuthResponse;
import ru.bykov.insidetest.model.dto.LoginDto;
import ru.bykov.insidetest.model.dto.SignUpDto;

public interface UserService {

    ResponseEntity<?> createUser(SignUpDto signUpDto);

    ResponseEntity<JWTAuthResponse> authenticateUser(LoginDto loginDto);
}
