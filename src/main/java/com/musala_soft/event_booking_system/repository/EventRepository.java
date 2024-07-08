package com.musala_soft.event_booking_system.repository;

import com.musala_soft.event_booking_system.models.Event;
import com.musala_soft.event_booking_system.models.Status;
import com.musala_soft.event_booking_system.openapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Query(value = "SELECT e FROM Event e WHERE (:name IS NULL OR :name LIKE CONCAT('%',:name,'%')) " + "OR (:category IS NULL OR e.category = :category) " + "OR e.date BETWEEN :startDate AND :endDate")
    List<Event> findAllEvents(String name, Category category, LocalDate startDate, LocalDate endDate);

    List<Event> findEventsByEventStatus(Status status);

}
