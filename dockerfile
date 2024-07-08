FROM openjdk:17

EXPOSE 9000

COPY ./target/event-booking-system-0.0.1-SNAPSHOT.jar event_booking_system.jar

ENTRYPOINT ["java","-jar","/event_booking_system.jar"]