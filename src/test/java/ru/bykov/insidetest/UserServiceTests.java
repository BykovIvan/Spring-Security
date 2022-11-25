package ru.bykov.insidetest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.bykov.insidetest.model.Role;
import ru.bykov.insidetest.model.User;
import ru.bykov.insidetest.model.dto.JWTAuthResponse;
import ru.bykov.insidetest.model.dto.LoginDto;
import ru.bykov.insidetest.model.dto.SignUpDto;
import ru.bykov.insidetest.repository.RoleRepository;
import ru.bykov.insidetest.repository.UserRepository;
import ru.bykov.insidetest.service.UserService;
import ru.bykov.insidetest.service.impl.UserServiceImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class UserServiceTests {

    @InjectMocks
    @Autowired
    private UserServiceImpl service;
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void saveUserTest() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("ivan");
        signUpDto.setPassword("password");
        signUpDto.setEmail("ivan@yandex.ru");

        User user = new User();
        user.setName(signUpDto.getName());
        user.setPassword(signUpDto.getPassword());
        user.setEmail(signUpDto.getEmail());

        Role role = new Role();
        role.setName("ROLE_USER");
        Set<Role> set = new HashSet<>();
        set.add(role);
        user.setRoles(set);
        Mockito
                .when(roleRepository.findByName(Mockito.anyString()))
                .thenReturn(Optional.of(role));
        Mockito
                .when(userRepository.existsByName(Mockito.anyString()))
                .thenReturn(false);
        Mockito
                .when(userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);
        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
        ResponseEntity<?> getStr = service.createUser(signUpDto);

        assertThat(getStr.getBody()).isEqualTo("User registered successfully");

        LoginDto loginDto = new LoginDto();
        loginDto.setName(signUpDto.getName());
        loginDto.setPassword(signUpDto.getPassword());

        ResponseEntity<JWTAuthResponse> authenticateUser = service.authenticateUser(loginDto);

        assertThat(authenticateUser.getBody()).isNotNull();

    }

}
