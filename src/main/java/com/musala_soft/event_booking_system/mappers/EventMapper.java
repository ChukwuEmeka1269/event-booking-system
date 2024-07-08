package com.musala_soft.event_booking_system.mappers;

import com.musala_soft.event_booking_system.models.Event;
import com.musala_soft.event_booking_system.openapi.model.EventRequestDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapper implements Mapper<Event, EventRequestDTO> {
    private final ModelMapper modelMapper;

    @Override
    public EventRequestDTO mapTo(Event event) {
        return (event == null) ? null : modelMapper.map(event, EventRequestDTO.class);
    }

    @Override
    public Event mapFrom(EventRequestDTO eventRequestDto) {
        return (eventRequestDto == null) ? null : modelMapper.map(eventRequestDto, Event.class);
    }
}

