package ru.bykov.insidetest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bykov.insidetest.model.dto.UserDto;
import ru.bykov.insidetest.service.UserService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/auth/token")
public class UserController {

    private final UserService userService;

    @PostMapping
    public String create(@Valid @RequestBody UserDto userSto) {
        log.info("Получен запрос к эндпоинту /auth/token. Метод POST");
        return userService.create(userSto);
    }

}
