package com.musala_soft.event_booking_system.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala_soft.event_booking_system.exceptions.UserNotFoundException;
import com.musala_soft.event_booking_system.jwt.JwtTokenProvider;

import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.openapi.model.Credentials;
import com.musala_soft.event_booking_system.repository.UserRepository;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public String authenticate(Credentials credentials) {

        AppUser user = userRepository.findUserByEmail(credentials.getEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User with email : %s not found.", credentials.getEmail())));

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
        } catch (Exception ex) {
            throw new BadCredentialsException(ex.getMessage());
        }
        if (authentication.isAuthenticated()) {
            String userJsonString = objectMapper.writeValueAsString(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtTokenProvider.generateToken(userJsonString);
        } else {
            throw new AuthException("User is not authenticated.");
        }

    }
}
