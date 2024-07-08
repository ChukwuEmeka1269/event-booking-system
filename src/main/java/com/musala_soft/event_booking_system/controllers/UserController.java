package com.musala_soft.event_booking_system.controllers;

import com.musala_soft.event_booking_system.openapi.api.UsersApi;
import com.musala_soft.event_booking_system.openapi.model.User;
import com.musala_soft.event_booking_system.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
public class UserController implements UsersApi {
    private final UserService userService;

    @Override
    @PostMapping("/users")
    public ResponseEntity<Void> usersPost(@Valid @RequestBody User user) {

        try {
            userService.createUser(user);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(CREATED).build();
    }
}
