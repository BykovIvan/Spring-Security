package ru.bykov.insidetest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bykov.insidetest.model.dto.JWTAuthResponse;
import ru.bykov.insidetest.model.dto.LoginDto;
import ru.bykov.insidetest.model.dto.SignUpDto;
import ru.bykov.insidetest.service.UserService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        log.info("Получен доступ к энпоинту /auth/signin, метод POST, аутентификация пользователя");
        return userService.authenticateUser(loginDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDto signUpDto) {
        log.info("Получен доступ к энпоинту /auth/signup, метод POST, регистрация пользователя");
        return userService.createUser(signUpDto);
    }
}
