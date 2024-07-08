package com.musala_soft.event_booking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class EventBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventBookingSystemApplication.class, args);
	}

}
