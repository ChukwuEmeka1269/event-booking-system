package com.musala_soft.event_booking_system.service;


import com.musala_soft.event_booking_system.models.Event;
import com.musala_soft.event_booking_system.openapi.model.EventRequestDTO;
import com.musala_soft.event_booking_system.openapi.model.EventResponseDTO;
import com.musala_soft.event_booking_system.openapi.model.TicketRequest;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    EventResponseDTO createEvent(EventRequestDTO eventRequestDTO);

    Event bookEvent(Long eventId);

    void reserveTicket(TicketRequest request, Long eventId);

    List<EventResponseDTO> viewEvents();

    List<EventResponseDTO> searchEvent(String name, LocalDate startDate, LocalDate endDate, String category);

    void cancelEvent(Long eventId);

    List<Event> fetchUpcomingEvents();


}
