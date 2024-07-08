package com.musala_soft.event_booking_system.service;

import com.musala_soft.event_booking_system.events.BookingEvent;
import com.musala_soft.event_booking_system.exceptions.AlreadyBookedEvenException;
import com.musala_soft.event_booking_system.exceptions.EventReservationException;
import com.musala_soft.event_booking_system.exceptions.EventStatusException;
import com.musala_soft.event_booking_system.mappers.EventDtoMapper;
import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.models.Event;
import com.musala_soft.event_booking_system.models.Role;
import com.musala_soft.event_booking_system.models.Status;
import com.musala_soft.event_booking_system.openapi.model.*;
import com.musala_soft.event_booking_system.repository.EventRepository;
import com.musala_soft.event_booking_system.repository.UserRepository;
import com.musala_soft.event_booking_system.service.impl.EventServiceImpl;
import com.musala_soft.event_booking_system.testUtils.TestUtils;
import com.musala_soft.event_booking_system.utils.AppUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.*;

import static com.musala_soft.event_booking_system.constants.APP_CONSTANT.*;
import static com.musala_soft.event_booking_system.constants.APP_CONSTANT.EVENT_ALREADY_BOOKED;
import static com.musala_soft.event_booking_system.constants.APP_CONSTANT.EVENT_RESERVATION_MSG;
import static com.musala_soft.event_booking_system.models.Status.PENDING;
import static com.musala_soft.event_booking_system.openapi.model.Category.GAME;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @InjectMocks
    @Spy
    private EventServiceImpl eventServiceUnderTest;

    @Mock
    EventRepository eventRepository;

    @Mock
     UserRepository userRepository;

     @Mock
     EventDtoMapper eventDtoMapper;
     @Mock
     ApplicationEventPublisher eventPublisher;
    private AppUser appUser;

    private Event event;

    private EventRequestDTO gameEventCreationRequest;

    private MockedStatic<AppUtil> mockStatic;

    private AutoCloseable closeable;


    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
        mockStatic.close();
    }


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockStatic = mockStatic(AppUtil.class);

        eventServiceUnderTest= new EventServiceImpl(eventRepository, userRepository, eventDtoMapper, eventPublisher );
        appUser = AppUser.builder()
                .id(1L)
                .name("Rexco")
                .email("emirex50@gmail.com")
                .password("Password1234")
                .createdEvents(new ArrayList<>())
                .bookedEvents(new ArrayList<>())
                .canceledEvents(new ArrayList<>())
                .reservedEvents(new ArrayList<>())
                .role(Role.USER)
                .build();

        gameEventCreationRequest = TestUtils.game();

        event =  Event.builder()
                .id(1L)
                .eventStatus(PENDING)
                .name("GAME EVENT")
                .createdBy(appUser)
                .description("Game event description")
                .availableAttendeesCount(1000)
                .date(now().plusDays(1).toLocalDate())
                .category(GAME)
                .build();//give back same as gameEventCreationRequest
        // set necessary fields for event

//        when(AppUtil.getCurrentlyLoggedInUser()).thenReturn(appUser);
        appUser.getCreatedEvents().add(event);
        // Mock the utility method to return the logged-in user
    }

    @Test
    void createEvent_shouldCreateAndSaveEvent_ThenReturnValidResponse() {

        //assign
        AppUser user = TestUtils.completeUserBuilder();
        when(AppUtil.getCurrentlyLoggedInUser()).thenReturn(user);
        Mockito.when(eventRepository.save(Mockito.any(Event.class))).thenReturn(event);
        //act
        EventResponseDTO eventCreationResponse = eventServiceUnderTest.createEvent(gameEventCreationRequest);
        //assert

        assertThat(eventCreationResponse).isNotNull();
        assertThat(eventCreationResponse.getName()).isEqualTo(gameEventCreationRequest.getName());
        assertThat(eventCreationResponse.getDate()).isEqualTo(gameEventCreationRequest.getDate());
        assertThat(eventCreationResponse.getCategory()).isEqualTo(gameEventCreationRequest.getCategory());
        assertThat(eventCreationResponse.getDescription()).isEqualTo(gameEventCreationRequest.getDescription());
        assertThat(eventCreationResponse.getAvailableAttendeesCount()).isEqualTo(gameEventCreationRequest.getAvailableAttendeesCount());


    }

    @Test
    void createEvent_ShouldThrowException_WhenEventDateIsBeforeToday() {
        //Arrange
        gameEventCreationRequest.setDate(LocalDate.now().minusDays(1));

        //Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventServiceUnderTest.createEvent(gameEventCreationRequest);
        });

        assertEquals("Event date cannot be in the past", exception.getMessage());
    }


    @Test
    void bookEvent_shouldBookValidEventSuccessfullyAndPublishBookEvent() {
        // Arrange
        Long eventId = 2L;
        event = TestUtils.event();
        event.setId(eventId);
        AppUser user = TestUtils.completeUserBuilder();

        when(userRepository.save(Mockito.any(AppUser.class))).thenReturn(user);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        when(eventRepository.save(Mockito.any(Event.class))).thenReturn(event);
        when(AppUtil.getCurrentlyLoggedInUser()).thenReturn(user);
        when(eventRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(event));

        Integer availableAttendeesCountBeforeBooking = event.getAvailableAttendeesCount();
        // Act
        Event bookedEvent = eventServiceUnderTest.bookEvent(eventId);
        Integer availableAttendeesCountAfterBooking = bookedEvent.getAvailableAttendeesCount();

        // Assert
        assertTrue(user.getBookedEvents().contains(event));
        assertThat(availableAttendeesCountBeforeBooking).isGreaterThan(availableAttendeesCountAfterBooking);
        verify(eventPublisher, times(1)).publishEvent(any(BookingEvent.class));
    }

    @Test
    void bookEvent_shouldThrowException_whenEventAlreadyBooked() {
        // Arrange
        Long eventId = 2L;
        event.setId(eventId);
        appUser.getBookedEvents().add(event);
        event.setBookedUsers(List.of(appUser));
        when(AppUtil.getCurrentlyLoggedInUser()).thenReturn(appUser);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(appUser));
        // Act & Assert
        AlreadyBookedEvenException exception = assertThrows(AlreadyBookedEvenException.class, () -> {
            eventServiceUnderTest.bookEvent(eventId);
        });

        assertEquals(String.format(EVENT_ALREADY_BOOKED, event.getName()), exception.getMessage());
        verify(eventPublisher, never()).publishEvent(any(BookingEvent.class));
    }

    @Test
    void reserveTicket_shouldReserveAvailableTicketsSuccessfully() {
        //Act
        final Integer maxAvailableSlots = 12;
        Long eventId = 1L;
        event.setId(eventId);
        event.setAvailableAttendeesCount(maxAvailableSlots);
        appUser.getBookedEvents().add(event);
        event.setBookedUsers(List.of(appUser));
        when(AppUtil.getCurrentlyLoggedInUser()).thenReturn(appUser);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(appUser));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        final TicketRequest ticketRequest = new TicketRequest(3);

        eventServiceUnderTest.reserveTicket(ticketRequest, eventId);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);

        Mockito.verify(eventRepository).save(eventCaptor.capture());

        Event captured = eventCaptor.getValue();

        assertEquals(captured.getAvailableAttendeesCount(), maxAvailableSlots - ticketRequest.getAttendeesCount());

    }

    @Test
    void event_shouldThrowExceptionWhenMaxSlotsReached() {
        //Arrange
        final int maxAvailableSlots = 2;
        Long eventId = 1L;
        event.setId(eventId);
        event.setAvailableAttendeesCount(maxAvailableSlots);
        appUser.getBookedEvents().add(event);
        event.setBookedUsers(List.of(appUser));

        //Act
        when(AppUtil.getCurrentlyLoggedInUser()).thenReturn(appUser);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(appUser));
        final TicketRequest ticketRequest = new TicketRequest(3);

        //Assert
        EventReservationException eventReservationException = assertThrows(EventReservationException.class,
                () -> eventServiceUnderTest.reserveTicket(ticketRequest, eventId));

        assertThat(eventReservationException.getMessage()).isEqualTo(String.format(EVENT_RESERVATION_MSG, Math.abs(maxAvailableSlots)));
    }
    @Test
    void event_ShouldThrowExceptionWhenCancelEventIsNotPending(){
        //Arrange
        Long eventId = 1L;
        event.setId(eventId);
        event.setEventStatus(Status.DONE);
        appUser.getBookedEvents().add(event);
        event.setBookedUsers(List.of(appUser));

        //Act
        when(AppUtil.getCurrentlyLoggedInUser()).thenReturn(appUser);
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(appUser));

        //When
        EventStatusException eventStatusException = assertThrows(EventStatusException.class,
                () -> eventServiceUnderTest.cancelEvent(eventId));

        assertThat(eventStatusException.getMessage()).isEqualTo(EVENT_STATUS_EX_MSG);
    }

    @Test
    void event_shouldUpdateEventWhenCancelled() {
        //Arrange
        final int maxAvailableSlots = 12;
        final Long eventId = 1L;
        event.setId(eventId);
        event.setAvailableAttendeesCount(maxAvailableSlots);
        appUser.getBookedEvents().add(event);
        event.setBookedUsers(List.of(appUser));

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(appUser));
        when(AppUtil.getCurrentlyLoggedInUser()).thenReturn(appUser);

        eventServiceUnderTest.cancelEvent(eventId);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);

        Mockito.verify(eventRepository).save(eventCaptor.capture());

        Event captured = eventCaptor.getValue();

        assertEquals(captured.getAvailableAttendeesCount(), maxAvailableSlots + 1);
        assertEquals(captured.getEventStatus(), Status.CANCELED);
        assertFalse(appUser.getBookedEvents().contains(event));
        assertTrue(appUser.getCanceledEvents().contains(event));

    }

    @Test
    void testViewBookedEvents() {
    }
}