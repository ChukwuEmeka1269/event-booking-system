package com.musala_soft.event_booking_system.mappers;

import com.musala_soft.event_booking_system.models.Event;
import com.musala_soft.event_booking_system.openapi.model.EventResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventDtoMapper implements Mapper<Event, EventResponseDTO> {

    private final ModelMapper modelMapper;

    @Override
    public Event mapFrom(EventResponseDTO event) {
        return (event == null) ? null : modelMapper.map(event, Event.class);
    }

    @Override
    public EventResponseDTO mapTo(Event event) {
        return (event == null) ? null : modelMapper.map(event, EventResponseDTO.class);
    }
}
