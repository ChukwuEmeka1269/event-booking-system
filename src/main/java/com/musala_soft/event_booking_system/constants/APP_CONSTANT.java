package com.musala_soft.event_booking_system.constants;


public interface APP_CONSTANT {

    String USER_CREATION_SUBJECT = "Welcome!!";
    String USER_EVENT_CREATION_SUBJECT = "Event creation success";
    String USER_EVENT_BOOKING_SUBJECT = "Congratulations!!";
    String USER_EVENT_CANCELLATION_SUBJECT = "Event Cancellation";

    String USER_UPCOMING_EVENT_SUBJECT = "Your event is Coming!!!";

    String TEMPLATE_WELCOME = "welcome";
    String TEMPLATE_CREATE_EVENT = "event_creation";
    String TEMPLATE_BOOK_EVENT = "event_booking";
    String TEMPLATE_CANCEL_EVENT = "event_cancel";

    String TEMPLATE_UPCOMING_EVENT = "upcoming_event";

    String EVENT_NOT_FOUND_MESSAGE = "Event with id : %s was not found.";
    String USER_NOT_FOUND_MSG = "User with id : %s was not found.";

    String USER_EMAIL_DOES_NOT_EXIST = "user with email : %s does not exist.";
    String EVENT_ALREADY_BOOKED = "You have already booked %s event before. ";
    String EVENT_STATUS_EX_MSG = "Event has already been CANCELED or is DONE";
    String EVENT_RESERVATION_MSG = "There are only %s tickets available";
    String USER_ALREADY_EXIST_MSG = "User already exist.";
    String EVENT_CREATION_EX = "Event date cannot be in the past";
}
