package com.musala_soft.event_booking_system.repository;

import com.musala_soft.event_booking_system.models.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
}
