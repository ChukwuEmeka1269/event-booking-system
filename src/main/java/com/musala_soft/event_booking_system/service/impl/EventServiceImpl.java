package com.musala_soft.event_booking_system.service.impl;

import com.musala_soft.event_booking_system.constants.APP_CONSTANT;
import com.musala_soft.event_booking_system.events.BookingEvent;
import com.musala_soft.event_booking_system.events.CancellationEvent;
import com.musala_soft.event_booking_system.events.EventCreationEvent;
import com.musala_soft.event_booking_system.exceptions.*;
import com.musala_soft.event_booking_system.mappers.EventDtoMapper;
import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.models.Event;
import com.musala_soft.event_booking_system.openapi.model.Category;
import com.musala_soft.event_booking_system.openapi.model.EventRequestDTO;
import com.musala_soft.event_booking_system.openapi.model.EventResponseDTO;
import com.musala_soft.event_booking_system.openapi.model.TicketRequest;
import com.musala_soft.event_booking_system.repository.EventRepository;
import com.musala_soft.event_booking_system.repository.UserRepository;
import com.musala_soft.event_booking_system.service.EventService;
import com.musala_soft.event_booking_system.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.musala_soft.event_booking_system.constants.APP_CONSTANT.*;
import static com.musala_soft.event_booking_system.constants.APP_CONSTANT.EVENT_NOT_FOUND_MESSAGE;
import static com.musala_soft.event_booking_system.constants.APP_CONSTANT.USER_NOT_FOUND_MSG;
import static com.musala_soft.event_booking_system.models.Status.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final EventDtoMapper eventDtoMapper;

    private final ApplicationEventPublisher eventPublisher;


    @Override
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        Event event = new Event(eventRequestDTO);


        AppUser loggedInUser = AppUtil.getCurrentlyLoggedInUser();

        LocalDate today = LocalDate.now();

        if (today.isAfter(event.getDate())) throw new IllegalArgumentException(APP_CONSTANT.EVENT_CREATION_EX);

        event.setCreatedBy(loggedInUser);
        Event savedEvent = eventRepository.save(event);
        loggedInUser.getCreatedEvents().add(event);

        userRepository.save(loggedInUser);
        EventCreationEvent eventCreationEvent = new EventCreationEvent(event);
        eventPublisher.publishEvent(eventCreationEvent);

        return savedEvent.mapTo();
    }


    @Override
    public Event bookEvent(Long eventId) {
        AppUser currentlyLoggedInUser = getUserById(AppUtil.getCurrentlyLoggedInUser().getId());

        Event event = getAvailablePendingEventById(eventId);

        boolean isEventAdded = currentlyLoggedInUser.getBookedEvents().contains(event);
        if (isEventAdded) {
            throw new AlreadyBookedEvenException(String.format(EVENT_ALREADY_BOOKED, event.getName()));
        }

        Map<String, Object> persistEventAndUser = persistBookedEventAndUser(currentlyLoggedInUser, event);

        BookingEvent bookingEvent = new BookingEvent(this, (AppUser) persistEventAndUser.get("user"), (Event) persistEventAndUser.get("event"));
        eventPublisher.publishEvent(bookingEvent);

        return (Event) persistEventAndUser.get("event");
    }

    @Override
    public void reserveTicket(TicketRequest request, Long eventId) {
        AppUser userFromContext = getUserById(AppUtil.getCurrentlyLoggedInUser().getId());

        Event availableEvent = getAvailablePendingEventById(eventId);
        AtomicReference<Event> eventAtomicReference = new AtomicReference<>(availableEvent);
        Event atomicEvent = eventAtomicReference.get();
        int availableAttendeesCount = atomicEvent.getAvailableAttendeesCount();
        int requestAttendeesCount = request.getAttendeesCount();
        int remainingAttendeesCount = availableAttendeesCount - requestAttendeesCount;
        if (remainingAttendeesCount < 0) {
            throw new EventReservationException(String.format(EVENT_RESERVATION_MSG, Math.abs(availableAttendeesCount)));
        }

        atomicEvent.setAvailableAttendeesCount(remainingAttendeesCount);
        eventRepository.save(atomicEvent);
        userFromContext.getReservedEvents().add(atomicEvent);
        userRepository.save(userFromContext);

    }


    @Override
    public List<EventResponseDTO> viewEvents() {
        AppUser currentlyLoggedInUser = getUserById(AppUtil.getCurrentlyLoggedInUser().getId());
        List<Event> bookedEvents = currentlyLoggedInUser.getBookedEvents();
        return bookedEvents.stream().map(Event::mapTo).collect(Collectors.toList());
    }


    @Override
    public List<EventResponseDTO> searchEvent(String name, LocalDate startDate, LocalDate endDate, String category) {

        return eventRepository.findAllEvents(name, Category.fromValue(category), startDate, endDate).stream().map(eventDtoMapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public void cancelEvent(Long eventId) {

        AppUser user = getUserById(AppUtil.getCurrentlyLoggedInUser().getId());

        Event foundEvent = user.getBookedEvents().stream().filter(event -> eventId.equals(event.getId())).findFirst().orElseThrow(() -> new EventNotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId)));

        if (!PENDING.equals(foundEvent.getEventStatus())) {
            throw new EventStatusException(EVENT_STATUS_EX_MSG);
        }
        log.info("PREVIOUS ATTENDEE COUNT BEFORE CANCELLATION ============= " + foundEvent.getAvailableAttendeesCount());
//        foundEvent.setEventStatus(CANCELED);
        foundEvent.setAvailableAttendeesCount(foundEvent.getAvailableAttendeesCount() + 1);
        log.info("CURRENT ATTENDEE COUNT AFTER CANCELLATION ============= " + foundEvent.getAvailableAttendeesCount());
        Event savedEvent = eventRepository.save(foundEvent);

        user.getBookedEvents().remove(foundEvent);
        user.getCanceledEvents().add(foundEvent);

        userRepository.save(user);

        CancellationEvent cancellationEvent = new CancellationEvent(this, user, savedEvent);
        eventPublisher.publishEvent(cancellationEvent);
    }

    @Override
    public List<Event> fetchUpcomingEvents() {
        return eventRepository.findEventsByEventStatus(PENDING);
    }

    public Event getAvailablePendingEventById(Long eventId) {
        Event pendingEvent = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId)));

        if (!PENDING.equals(pendingEvent.getEventStatus())) throw new EventStatusException(EVENT_STATUS_EX_MSG);
        return pendingEvent;
    }

    private AppUser getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));
    }

    public Map<String, Object> persistBookedEventAndUser(AppUser user, Event event) {
        AtomicInteger atomicAvailableAttendeeCount = new AtomicInteger(event.getAvailableAttendeesCount());
        event.setAvailableAttendeesCount(atomicAvailableAttendeeCount.decrementAndGet());
        Event savedEvent = eventRepository.save(event);
        user.getBookedEvents().add(event);
        AppUser savedUser = userRepository.save(user);

        Map<String, Object> props = new HashMap<>();
        props.put("user", savedUser);
        props.put("event", savedEvent);
        return props;
    }


    @Scheduled(cron = "${app.update_done_status.job}")
    void updateEventStatusFromPendingToDone() {
        List<Event> updatedEvents = eventRepository.findEventsByEventStatus(PENDING).stream().filter(event -> LocalDateTime.now().minusDays(1).toLocalDate().equals(event.getDate())).peek(event -> event.setEventStatus(DONE)).toList();
        eventRepository.saveAll(updatedEvents);
    }

    @Scheduled(cron = "${app.update_ongoing_status.job}")
    void updateOngoingEventsStatus() {
        List<Event> updatedEvents = eventRepository.findEventsByEventStatus(PENDING).stream().filter(event -> LocalDateTime.now().equals(event.getDate().atStartOfDay())).peek(event -> event.setEventStatus(ONGOING)).toList();
        eventRepository.saveAll(updatedEvents);
    }

}
