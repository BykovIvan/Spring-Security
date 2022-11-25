package ru.bykov.insidetest.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bykov.insidetest.jwt.JwtTokenProvider;
import ru.bykov.insidetest.model.Role;
import ru.bykov.insidetest.model.User;
import ru.bykov.insidetest.model.dto.JWTAuthResponse;
import ru.bykov.insidetest.model.dto.LoginDto;
import ru.bykov.insidetest.model.dto.SignUpDto;
import ru.bykov.insidetest.repository.RoleRepository;
import ru.bykov.insidetest.repository.UserRepository;
import ru.bykov.insidetest.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;


    @Override
    public ResponseEntity<?> createUser(SignUpDto signUpDto) {
        // add check for username exists in a DB
        if (userRepository.existsByName(signUpDto.getName())) {
            return new ResponseEntity<>("Name is already taken!", HttpStatus.BAD_REQUEST);
        }
        // add check for email exists in DB
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        // create user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        if (signUpDto.getRoles() == null || signUpDto.getRoles().isEmpty() ) {
            roles.add(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Нет такой роли в таблице")));
        } else {
            for (String name : signUpDto.getRoles()) {
                roles.add(roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Нет такой роли в таблице")));
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<JWTAuthResponse> authenticateUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getName(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(JWTAuthResponse.builder().token(token).build());
    }
}
