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
import ru.bykov.insidetest.payload.JWTAuthResponse;
import ru.bykov.insidetest.payload.LoginDto;
import ru.bykov.insidetest.payload.SignUpDto;
import ru.bykov.insidetest.repository.RoleRepository;
import ru.bykov.insidetest.repository.UserRepository;
import ru.bykov.insidetest.service.UserService;

import java.util.Collections;

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
        if(userRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        // add check for email exists in DB
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        // create user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        Role roles;
        if (signUpDto.getRole().isEmpty()){
            roles = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Нет такой роли в таблице"));
        } else {
            roles = roleRepository.findByName(signUpDto.getRole()).orElseThrow(() -> new RuntimeException("Нет такой роли в таблице"));
        }
        user.setRoles(Collections.singleton(roles));
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
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }
}
