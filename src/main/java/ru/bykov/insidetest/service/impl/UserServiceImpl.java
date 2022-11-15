package ru.bykov.insidetest.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.insidetest.model.dto.UserDto;
import ru.bykov.insidetest.repository.UserRepository;
import ru.bykov.insidetest.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public String create(UserDto userSto) {

        return null;
    }
}
