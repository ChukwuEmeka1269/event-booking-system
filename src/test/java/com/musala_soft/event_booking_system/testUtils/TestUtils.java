package com.musala_soft.event_booking_system.testUtils;

import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.models.Event;
import com.musala_soft.event_booking_system.models.Role;

import com.musala_soft.event_booking_system.openapi.model.EventRequestDTO;
import com.musala_soft.event_booking_system.openapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static com.musala_soft.event_booking_system.models.Status.*;
import static com.musala_soft.event_booking_system.models.Status.PENDING;
import static com.musala_soft.event_booking_system.openapi.model.Category.*;
import static java.time.LocalDateTime.*;


@RequiredArgsConstructor
public class TestUtils {

    private final PasswordEncoder passwordEncoder;
    public static User validUserCreationRequestBuilder(){
        return new User("Esther Lawal", "esther@gmail.com", "password1234");
    }

    public static User inValidUserCreationRequestBuilder(){
        return new User(null, "esthergmail.com", "password1234");
    }

    public static AppUser userBuilder(){
        return AppUser.builder()
                .id(1L)
                .name(validUserCreationRequestBuilder().getName())
                .email(validUserCreationRequestBuilder().getEmail())
                .password(validUserCreationRequestBuilder().getPassword())
                .build();
    }

    public static AppUser completeUserBuilder(){
        return AppUser.builder()
                .id(1L)
                .name(validUserCreationRequestBuilder().getName())
                .email(validUserCreationRequestBuilder().getEmail())
                .password(validUserCreationRequestBuilder().getPassword())
                .role(Role.USER)
                .createdEvents(createdEvents())
                .reservedEvents(reservedEvents())
                .canceledEvents(cancelledEvents())
                .bookedEvents(bookedEvents())
                .build();
    }


    public static AppUser userWithBookedEventBuilder(){
        return AppUser.builder()
                .id(1L)
                .name(validUserCreationRequestBuilder().getName())
                .email(validUserCreationRequestBuilder().getEmail())
                .password(validUserCreationRequestBuilder().getPassword())
                .role(Role.USER)
                .bookedEvents(bookedEvents())
                .build();
    }

    public static AppUser userWithCreatedEventBuilder(){
        return AppUser.builder()
                .id(1L)
                .name(validUserCreationRequestBuilder().getName())
                .email(validUserCreationRequestBuilder().getEmail())
                .password(validUserCreationRequestBuilder().getPassword())
                .role(Role.USER)
                .bookedEvents(bookedEvents())
                .build();
    }

    public static AppUser userWithReservedEventBuilder(){
        return AppUser.builder()
                .id(1L)
                .name(validUserCreationRequestBuilder().getName())
                .email(validUserCreationRequestBuilder().getEmail())
                .password(validUserCreationRequestBuilder().getPassword())
                .role(Role.USER)
                .reservedEvents(reservedEvents())
                .build();
    }

    public static AppUser userWithCancelledEventBuilder(){
        return AppUser.builder()
                .id(1L)
                .name(validUserCreationRequestBuilder().getName())
                .email(validUserCreationRequestBuilder().getEmail())
                .password(validUserCreationRequestBuilder().getPassword())
                .role(Role.USER)
                .canceledEvents(cancelledEvents())
                .build();
    }


    public static Event event(){
        return Event.builder()
                .eventStatus(PENDING)
                .name("GAME EVENT")
                .availableAttendeesCount(1000)
                .date(now().plusDays(1).toLocalDate())
                .category(GAME)
                .build();

    }

    public static Event event1(){
        return Event.builder()
                .eventStatus(PENDING)
                .name("EVENT 2 IN 3 DAYS")
                .availableAttendeesCount(200)
                .description("EVENT 2 Desc.")
                .date(now().plusDays(3).toLocalDate())
                .category(CONFERENCE)
                .build();

    }

    public static Event event2(){
        return Event.builder()
                .eventStatus(PENDING)
                .name("EVENT 3 IN 1 DAY")
                .availableAttendeesCount(200)
                .date(now().plusDays(1).toLocalDate())
                .category(CONCERT)
                .build();

    }


    public static EventRequestDTO concert() {
        return new EventRequestDTO("CONCERT EVENT 2 IN 1 DAY",
                now().plusDays(1).toLocalDate(),
                200, "200",
                CONCERT);
    }


    public static EventRequestDTO game() {
        return new EventRequestDTO("GAME EVENT",
                now().plusDays(1).toLocalDate(),
                1000, "Game event description",
                GAME);
    }

    public static EventRequestDTO conference() {
        return new EventRequestDTO("CONFERENcE EVENT 3 IN 1 DAY",
                now().plusDays(1).toLocalDate(),
                500, "200",
                GAME);
    }



    public static List<Event> bookedEvents(){
        List<Event> events = new ArrayList<>();
        Event event1 = Event.builder()
                .name("BOOKED EVENT 1")
                .date(now().toLocalDate())
                .category(GAME)
                .eventStatus(PENDING)
                .availableAttendeesCount(100)
                .build();

        Event event2 = Event.builder()
                .name("BOOKED EVENT 2")
                .date(now().toLocalDate())
                .category(CONCERT)
                .eventStatus(PENDING)
                .availableAttendeesCount(200)
                .build();

        Event event3 = Event.builder()
                .name("BOOKED EVENT 3")
                .date(now().toLocalDate())
                .category(CONFERENCE)
                .eventStatus(PENDING)
                .availableAttendeesCount(1000)
                .build();
        Event event4 = Event.builder()
                .name("BOOKED EVENT 4")
                .date(now().toLocalDate())
                .category(GAME)
                .eventStatus(PENDING)
                .availableAttendeesCount(100)
                .build();

        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);
        return events;
    }

    public static List<Event> createdEvents(){
        List<Event> eventList = new ArrayList<>();
        Event event1 = Event.builder()
                .name("CREATED EVENT 1")
                .date(now().toLocalDate())
                .category(GAME)
                .eventStatus(PENDING)
                .availableAttendeesCount(100)
                .build();
        Event event2 = Event.builder()
                .name("CREATED EVENT 2")
                .date(now().toLocalDate())
                .category(CONCERT)
                .eventStatus(PENDING)
                .availableAttendeesCount(200)
                .build();
        Event event3 = Event.builder()
                .name("CREATED EVENT 3")
                .date(now().toLocalDate())
                .category(CONFERENCE)
                .eventStatus(PENDING)
                .availableAttendeesCount(1000)
                .build();
        Event event4 = Event.builder()
                .name("CREATED EVENT 4")
                .date(now().toLocalDate())
                .category(GAME)
                .eventStatus(PENDING)
                .availableAttendeesCount(100)
                .build();

        eventList.add(event1);
        eventList.add(event2);
        eventList.add(event3);
        eventList.add(event4);

        return eventList;
    }

    public static List<Event> cancelledEvents(){
        return List.of(
                Event.builder()
                        .name("CANCELLED EVENT 1")
                        .date(now().toLocalDate())
                        .category(GAME)
                        .eventStatus(CANCELED)
                        .availableAttendeesCount(100)
                        .build(),
                Event.builder()
                        .name("CANCELLED EVENT 2")
                        .date(now().toLocalDate())
                        .category(CONCERT)
                        .eventStatus(CANCELED)
                        .availableAttendeesCount(200)
                        .build(),
                Event.builder()
                        .name("CANCELLED EVENT 3")
                        .date(now().toLocalDate())
                        .category(CONFERENCE)
                        .eventStatus(CANCELED)
                        .availableAttendeesCount(1000)
                        .build(),
                Event.builder()
                        .name("CANCELLED EVENT 4")
                        .date(now().toLocalDate())
                        .category(GAME)
                        .eventStatus(CANCELED)
                        .availableAttendeesCount(100)
                        .build()
        );
    }

    public static List<Event> reservedEvents(){
        return List.of(
                Event.builder()
                        .name("RESERVED EVENT 1")
                        .date(now().toLocalDate())
                        .category(GAME)
                        .eventStatus(PENDING)
                        .description("Reserved game event ")
                        .availableAttendeesCount(100)
                        .build(),
                Event.builder()
                        .name("RESERVED EVENT 2")
                        .date(now().toLocalDate())
                        .category(CONCERT)
                        .description("Reserved concert event")
                        .eventStatus(PENDING)
                        .availableAttendeesCount(200)
                        .build(),
                Event.builder()
                        .name("RESERVED EVENT 3")
                        .date(now().toLocalDate())
                        .category(CONFERENCE)
                        .eventStatus(PENDING)
                        .description("Reserved conference event")
                        .availableAttendeesCount(1000)
                        .build(),
                Event.builder()
                        .name("RESERVED EVENT 4")
                        .date(now().toLocalDate())
                        .category(GAME)
                        .eventStatus(PENDING)
                        .description("Reserved game event")
                        .availableAttendeesCount(100)
                        .build()
        );
    }


}
