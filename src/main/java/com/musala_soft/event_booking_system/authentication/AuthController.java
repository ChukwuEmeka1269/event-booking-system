package com.musala_soft.event_booking_system.authentication;

import com.musala_soft.event_booking_system.openapi.api.AuthApi;
import com.musala_soft.event_booking_system.openapi.model.Credentials;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    @PostMapping("/auth")
    public ResponseEntity<Void> authPost(Credentials credentials) {
        String token = authService.authenticate(credentials);
        return ResponseEntity.ok().header("Authorization", token).build();
    }
}
