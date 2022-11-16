package ru.bykov.insidetest.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public class JwtUserDetailsService implements UserDetailsService {

    public static final String USER = "USER";
    public static final String ROLE_USER = "ROLE_" + USER;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final Client client = clientRepository.findByLogin(username).orElseThrow(
                () -> new UsernameNotFoundException("User " + username + " not found"));
        return new User(username, client.getHash(), Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER)));
    }
}
