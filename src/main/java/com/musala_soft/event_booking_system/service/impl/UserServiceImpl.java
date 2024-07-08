package com.musala_soft.event_booking_system.service.impl;

import com.musala_soft.event_booking_system.events.UserCreationEvent;
import com.musala_soft.event_booking_system.exceptions.UserAlreadyExistException;
import com.musala_soft.event_booking_system.mappers.UserMapper;
import com.musala_soft.event_booking_system.models.AppUser;

import com.musala_soft.event_booking_system.models.Role;
import com.musala_soft.event_booking_system.openapi.model.User;
import com.musala_soft.event_booking_system.repository.UserRepository;
import com.musala_soft.event_booking_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.musala_soft.event_booking_system.constants.APP_CONSTANT.USER_ALREADY_EXIST_MSG;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public AppUser createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException(USER_ALREADY_EXIST_MSG);
        }

        AppUser appUser = userMapper.mapFrom(user);
        appUser.setPassword(passwordEncoder.encode(user.getPassword()));
        appUser.setRole(Role.USER);

        AppUser savedUser = userRepository.save(appUser);

        //an event is created each time a new user registers on the platform and published for the event listeners to send a welcome email to that user.
        UserCreationEvent userCreationEvent = new UserCreationEvent(this, appUser);
        eventPublisher.publishEvent(userCreationEvent);

        return savedUser;
    }
}
