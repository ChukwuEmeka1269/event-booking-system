# # CREATE DATABASE IF NOT EXISTS event_booking_db;
#
# USE event_booking_db;
#
# CREATE TABLE IF NOT EXISTS `users` (
#                        id INT AUTO_INCREMENT PRIMARY KEY,
#                        `name` VARCHAR(255) NOT NULL,
#                        `email` VARCHAR(255) NOT NULL UNIQUE,
#                        `password` VARCHAR(255) NOT NULL,
#                        `role` ENUM('ADMIN', 'USER'),
#                        `created_date` TIMESTAMP NOT NULL,
#                        `last_modified_date` TIMESTAMP DEFAULT NULL
# );
#
#
#
# CREATE TABLE IF NOT EXISTS `event` (
#                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
#                        `name` VARCHAR(100) NOT NULL,
#                        `date` DATE NOT NULL,
#                        `available_attendees_count` INT NOT NULL CHECK (available_attendees_count >= 1 AND available_attendees_count <= 1000),  -- Enforce min/max values
#                        `description` VARCHAR(500),
#                        `category_enum` ENUM('CONCERT', 'CONFERENCE', 'GAME'),  -- Use ENUM for category options
#                        `event_status` ENUM('CANCELED', 'PENDING', 'COMPLETED'),
#                        `created_date` TIMESTAMP NOT NULL,
#                        `last_modified_date` TIMESTAMP DEFAULT NULL
# );

#
# alter table event
#     drop
#         foreign key FK31rxexkqqbeymnpw4d3bf9vsy
#     Hibernate:
# alter table event
#     drop
#         foreign key FK31rxexkqqbeymnpw4d3bf9vsy
#     2024-07-05T07:19:33.877+01:00 DEBUG 64416 --- [event-booking-system] [           main] org.hibernate.SQL                        :
# drop table if exists event
#     Hibernate:
# drop table if exists event
#     2024-07-05T07:19:33.888+01:00 DEBUG 64416 --- [event-booking-system] [           main] org.hibernate.SQL                        :
# drop table if exists users
#     Hibernate:
# drop table if exists users
#     2024-07-05T07:19:33.900+01:00 DEBUG 64416 --- [event-booking-system] [           main] org.hibernate.SQL                        :
# create table event (
#                        available_attendees_count integer check ((available_attendees_count>=1) and (available_attendees_count<=1000)),
#                        category tinyint check (category between 0 and 2),
#                        date date not null,
#                        event_status tinyint check (event_status between 0 and 2),
#                        created_date datetime(6) not null,
#                        id bigint not null auto_increment,
#                        last_modified_date datetime(6),
#                        user_id bigint,
#                        name varchar(100) not null,
#                        description varchar(500),
#                        primary key (id)
# ) engine=InnoDB
#     Hibernate:
# create table event (
#                        available_attendees_count integer check ((available_attendees_count>=1) and (available_attendees_count<=1000)),
#                        category tinyint check (category between 0 and 2),
#                        date date not null,
#                        event_status tinyint check (event_status between 0 and 2),
#                        created_date datetime(6) not null,
#                        id bigint not null auto_increment,
#                        last_modified_date datetime(6),
#                        user_id bigint,
#                        name varchar(100) not null,
#                        description varchar(500),
#                        primary key (id)
# ) engine=InnoDB
#     2024-07-05T07:19:33.913+01:00 DEBUG 64416 --- [event-booking-system] [           main] org.hibernate.SQL                        :
# create table users (
#                        created_date datetime(6) not null,
#                        id bigint not null auto_increment,
#                        last_modified_date datetime(6),
#                        name varchar(100),
#                        email varchar(255),
#                        password varchar(255),
#                        role enum ('ADMIN','USER'),
#                        primary key (id)
# ) engine=InnoDB
#     Hibernate:
# create table users (
#                        created_date datetime(6) not null,
#                        id bigint not null auto_increment,
#                        last_modified_date datetime(6),
#                        name varchar(100),
#                        email varchar(255),
#                        password varchar(255),
#                        role enum ('ADMIN','USER'),
#                        primary key (id)
# ) engine=InnoDB
#     2024-07-05T07:19:33.922+01:00 DEBUG 64416 --- [event-booking-system] [           main] org.hibernate.SQL                        :
# alter table event
#     add constraint FK31rxexkqqbeymnpw4d3bf9vsy
#         foreign key (user_id)
#             references users (id)
#     Hibernate:
# alter table event
#     add constraint FK31rxexkqqbeymnpw4d3bf9vsy
#         foreign key (user_id)
#             references users (id)