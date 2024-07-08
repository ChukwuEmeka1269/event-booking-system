package com.musala_soft.event_booking_system.exceptions;

import jakarta.security.auth.message.AuthException;

import org.hibernate.LazyInitializationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationTokenException.class)
    public ResponseEntity<ExceptionResponse<?>> handleAuthenticationTokenException(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse<?>> handleUserNameNotFoundException(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LazyInitializationException.class)
    public ResponseEntity<ExceptionResponse<?>> handleLazyInitializationException(LazyInitializationException ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionResponse<?>> handleAuthExceptions(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse<?>> handleBadCredentialException(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingEmailTemplateException.class)
    public ResponseEntity<ExceptionResponse<?>> handleMailSendingException(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EventStatusException.class)
    public ResponseEntity<ExceptionResponse<?>> handleEventStatusException(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EventReservationException.class)
    public ResponseEntity<ExceptionResponse<?>> handleEventReservationException(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AlreadyBookedEvenException.class)
    public ResponseEntity<ExceptionResponse<?>> handleAlreadyBookedEventException(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse<?>> handleUserAlreadyExistException(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse<?>> handleIllegalArgumentException(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ExceptionResponse<?>> handleEventNotFoundException(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse<?>> handleUserNotFoundException(Exception ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ErrorDetails apiError = new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    private ResponseEntity<ExceptionResponse<?>> getExceptionResponseResponseEntity(Exception ex, HttpStatus httpStatus) {

        var exceptionResponse = new ExceptionResponse<String>();
        String errorDescription = ex.getMessage();
        if (Objects.isNull(errorDescription)) errorDescription = ex.toString();
        exceptionResponse.setMessage(errorDescription);
        exceptionResponse.setStatus(httpStatus.value());
        exceptionResponse.setDateTimestamp(new Date());
        exceptionResponse.setData(null);
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }
}
