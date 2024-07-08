package com.musala_soft.event_booking_system.service;

import com.musala_soft.event_booking_system.events.UserCreationEvent;
import com.musala_soft.event_booking_system.exceptions.UserAlreadyExistException;
import com.musala_soft.event_booking_system.mappers.UserMapper;
import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.models.Event;

import com.musala_soft.event_booking_system.openapi.model.User;
import com.musala_soft.event_booking_system.repository.UserRepository;
import com.musala_soft.event_booking_system.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.musala_soft.event_booking_system.models.Status.*;
import static com.musala_soft.event_booking_system.openapi.model.Category.*;
import static java.time.LocalDateTime.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl serviceUnderTest;

    @Mock
     UserRepository userRepository;
    @Mock
     PasswordEncoder passwordEncoder;
    @Mock
     UserMapper userMapper;
    @Mock
     ApplicationEventPublisher eventPublisher;

    private User validUser;

    private User invalidUser;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        openMocks(this);
        serviceUnderTest= new UserServiceImpl(userRepository, passwordEncoder, userMapper, eventPublisher);
        validUser = validUserCreationRequestBuilder();
        appUser = userBuilder();
        invalidUser = inValidUserCreationRequestBuilder();
    }

//    @Test
//    void should_test_valid_user_creation_success(){
//        //given
//        AppUser mockUser = Mockito.mock(AppUser.class);
//        //when
//        Mockito.when(appUser.getPassword()).thenReturn(validUser.getPassword());
//        Mockito.when(serviceUnderTest.createUser(validUser)).thenReturn(appUser);
//        //then
//        org.assertj.core.api.Assertions.assertThat(userBuilder().getName()).isEqualTo(validUser.getName());
//        org.assertj.core.api.Assertions.assertThat(userBuilder().getEmail()).isEqualTo(validUser.getEmail());
//        org.assertj.core.api.Assertions.assertThat(userBuilder().getPassword()).isEqualTo(validUser.getPassword());
//    }

    @Test
    void createUser_ShouldThrowException_WhenUserAlreadyExists() {
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(true);

        Exception exception = assertThrows(UserAlreadyExistException.class, () -> {
            serviceUnderTest.createUser(validUser);
        });

        assertEquals("User already exist.", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail(validUser.getEmail());
        verify(userRepository, never()).save(any(AppUser.class));
    }


    @Test
    void createUser_ShouldCreateUser_WhenUserDoesNotExist() {
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(false);
        when(userMapper.mapFrom(validUser)).thenReturn(appUser);
        when(passwordEncoder.encode(validUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(appUser)).thenReturn(appUser);

        AppUser createdUser = serviceUnderTest.createUser(validUser);

        assertNotNull(createdUser);
        assertEquals(appUser.getEmail(), createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        verify(userRepository, times(1)).existsByEmail(validUser.getEmail());
        verify(userRepository, times(1)).save(appUser);
        verify(eventPublisher, times(1)).publishEvent(any(UserCreationEvent.class));
    }

    @Test
    void createUser_ShouldNotCreateUser_WhenInvalidUser() {
        when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        when(userMapper.mapFrom(Mockito.any(User.class))).thenReturn(appUser);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        when(userRepository.save(Mockito.any(AppUser.class))).thenReturn(appUser);

        AppUser createdUser = serviceUnderTest.createUser(validUser);

        assertNotNull(createdUser);
        assertEquals(appUser.getEmail(), createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        verify(userRepository, times(1)).existsByEmail(validUser.getEmail());
        verify(userRepository, times(1)).save(appUser);
        verify(eventPublisher, times(1)).publishEvent(any(UserCreationEvent.class));
    }

    private User validUserCreationRequestBuilder(){
        return new User("Esther Lawal", "esther@gmail.com", "password1234");
    }

    private User inValidUserCreationRequestBuilder(){
        return new User(null, "esthergmail.com", "password1234");
    }

    private  AppUser userBuilder(){
        return AppUser.builder()
                .id(1L)
                .name(validUserCreationRequestBuilder().getName())
                .email(validUserCreationRequestBuilder().getEmail())
                .password(passwordEncoder.encode(validUserCreationRequestBuilder().getPassword()))
                .build();
        //other fills defaults to []
    }

    private List<Event> bookedEvents(){
        return List.of(
                Event.builder()
                        .name("EVENT 1")
                        .date(now().toLocalDate())
                        .category(GAME)
                        .eventStatus(PENDING)
                        .availableAttendeesCount(100)
                        .build(),
                Event.builder()
                        .name("EVENT 2")
                        .date(now().toLocalDate())
                        .category(CONCERT)
                        .eventStatus(PENDING)
                        .availableAttendeesCount(200)
                        .build(),
                Event.builder()
                        .name("EVENT 3")
                        .date(now().toLocalDate())
                        .category(CONFERENCE)
                        .eventStatus(PENDING)
                        .availableAttendeesCount(1000)
                        .build(),
                Event.builder()
                        .name("EVENT 4")
                        .date(now().toLocalDate())
                        .category(GAME)
                        .eventStatus(PENDING)
                        .availableAttendeesCount(100)
                        .build()
        );
    }

    private List<Event> createdEvents(){
        return List.of(
                Event.builder()
                        .name("EVENT 1")
                        .date(now().toLocalDate())
                        .category(GAME)
                        .eventStatus(PENDING)
                        .availableAttendeesCount(100)
                        .build(),
                Event.builder()
                        .name("EVENT 2")
                        .date(now().toLocalDate())
                        .category(CONCERT)
                        .eventStatus(PENDING)
                        .availableAttendeesCount(200)
                        .build(),
                Event.builder()
                        .name("EVENT 3")
                        .date(now().toLocalDate())
                        .category(CONFERENCE)
                        .eventStatus(PENDING)
                        .availableAttendeesCount(1000)
                        .build(),
                Event.builder()
                        .name("EVENT 4")
                        .date(now().toLocalDate())
                        .category(GAME)
                        .eventStatus(PENDING)
                        .availableAttendeesCount(100)
                        .build()
        );
    }

}