package com.musala_soft.event_booking_system.controllers;


import com.musala_soft.event_booking_system.openapi.api.EventsApi;
import com.musala_soft.event_booking_system.openapi.model.EventRequestDTO;
import com.musala_soft.event_booking_system.openapi.model.EventResponseDTO;
import com.musala_soft.event_booking_system.openapi.model.EventsPost201Response;
import com.musala_soft.event_booking_system.openapi.model.TicketRequest;
import com.musala_soft.event_booking_system.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventController implements EventsApi {

    private final EventService eventService;

    @Override
    @PostMapping("/events")
    public ResponseEntity<EventsPost201Response> eventsPost(@Valid @RequestBody EventRequestDTO event) {
        EventResponseDTO createdEvent = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EventsPost201Response().eventId(createdEvent.getId()));
    }

    @PostMapping("/events/{eventId}/book")
    public ResponseEntity<Void> bookEvent(@PathVariable Long eventId) {
        eventService.bookEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/events/{eventId}/tickets")
    public ResponseEntity<Void> eventsEventIdTicketsPost(@PathVariable Long eventId, @RequestBody TicketRequest ticketRequest) {
        eventService.reserveTicket(ticketRequest, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping("/events")
    public ResponseEntity<List<EventResponseDTO>> eventsGet(@RequestParam String name, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate endDate, @RequestParam String category) {
        return ResponseEntity.ok(eventService.searchEvent(name, startDate, endDate, category));
    }

    @PostMapping("/events/{eventId}/cancel")
    public ResponseEntity<Void> cancelEvent(@PathVariable Long eventId) {
        eventService.cancelEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/events/view")
    public ResponseEntity<List<EventResponseDTO>> view() {
        return ResponseEntity.ok(eventService.viewEvents());
    }
}
