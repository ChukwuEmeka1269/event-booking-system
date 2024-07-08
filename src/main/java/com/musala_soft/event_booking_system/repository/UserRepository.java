package com.musala_soft.event_booking_system.repository;

import com.musala_soft.event_booking_system.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findUserByEmail(String email);

    Optional<AppUser> findByNameOrEmail(String name, String email);

    boolean existsByEmail(String email);
}
