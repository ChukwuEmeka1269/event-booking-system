package com.musala_soft.event_booking_system.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.musala_soft.event_booking_system.openapi.model.Category;
import com.musala_soft.event_booking_system.openapi.model.EventRequestDTO;
import com.musala_soft.event_booking_system.openapi.model.EventResponseDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("name")
    @NotBlank(message = "Name is mandatory")
    @Size(max = 100)
    private String name;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @Min(1L)
    @Max(1000L)
    private Integer availableAttendeesCount;
    @Size(max = 500, message = "Description should not be more than 500 characters.")
    private String description;

    private Category category;

    private Status eventStatus;

    @ManyToOne
    @JoinColumn(name = "createdby_id")
    private AppUser createdBy;

    @ManyToMany(mappedBy = "bookedEvents", fetch = FetchType.EAGER)
    private List<AppUser> bookedUsers = new ArrayList<>();

    @ManyToMany(mappedBy = "reservedEvents", fetch = FetchType.EAGER)
    private List<AppUser> reservationUsers = new ArrayList<>();

    @ManyToMany(mappedBy = "canceledEvents",fetch = FetchType.EAGER)
    private List<AppUser> cancelledBookingUsers = new ArrayList<>();


    public Event(EventRequestDTO requestDTO) {
        this.name = requestDTO.getName();
        this.date = requestDTO.getDate();
        this.eventStatus = Status.PENDING;
        this.category = requestDTO.getCategory();
        this.description = requestDTO.getDescription();
        this.availableAttendeesCount = requestDTO.getAvailableAttendeesCount();
    }

    public EventResponseDTO mapTo() {
        EventResponseDTO responseDTO = new EventResponseDTO();
        responseDTO.setName(this.name);
        responseDTO.setCategory(this.category);
        responseDTO.setDate(this.date);
        responseDTO.setDescription(this.description);
        responseDTO.setAvailableAttendeesCount(this.availableAttendeesCount);
        responseDTO.setId(this.id);

        return responseDTO;
    }


}
