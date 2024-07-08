package com.musala_soft.event_booking_system.service.impl;


import com.musala_soft.event_booking_system.exceptions.UsernameNotFoundException;

import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


import static com.musala_soft.event_booking_system.constants.APP_CONSTANT.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public AppUser loadUserByUsername(String nameOrEmail) throws UsernameNotFoundException {
        return userRepository.findByNameOrEmail(nameOrEmail, nameOrEmail).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, nameOrEmail)));
    }
}
