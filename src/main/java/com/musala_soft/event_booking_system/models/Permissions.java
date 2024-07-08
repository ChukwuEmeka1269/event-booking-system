package com.musala_soft.event_booking_system.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissions {
    ADMIN_CREATE("admin:create"), USER_CREATE("user:create");

    @Getter
    private final String permission;
}
