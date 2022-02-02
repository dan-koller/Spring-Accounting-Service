package io.github.dankoller.springaccountingservice.controller;

import io.github.dankoller.springaccountingservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UniqueConstraintException.class)
    public ResponseEntity<?> handleUniqueConstraintException(UniqueConstraintException ex, HttpServletRequest request){
        final Map<String, ?> response = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "path", request.getServletPath(),
                "timestamp", LocalDateTime.now()
        );

        System.out.println("\nExceptionHandler_handleUniqueConstraintException:");
        System.out.println(response +"\n");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadNewPasswordException.class)
    public ResponseEntity<?> handleBadPasswordException(BadNewPasswordException ex, HttpServletRequest request) {
        final Map<String, ?> response = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "path", request.getServletPath(),
                "timestamp", LocalDateTime.now()
        );

        System.out.println("\nExceptionHandler_handleBadPasswordException:");
        System.out.println(response +"\n");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(TestException.class)
    public ResponseEntity<?> handleTestException(TestException ex, HttpServletRequest request) {
        // https://datatracker.ietf.org/doc/html/rfc2324#section-2.3.2

        final Map<String, ?> logMessage = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.I_AM_A_TEAPOT.value(),
                "error", HttpStatus.I_AM_A_TEAPOT.getReasonPhrase(),
                "path", request.getServletPath(),
                "timestamp", LocalDateTime.now()
        );

        System.out.println("\nExceptionHandler_handleTestException:");
        System.out.println(logMessage +"\n");

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(Map.of());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public  ResponseEntity<?> handleUnauthorizedAccessException(UnauthorizedAccessException  ex, HttpServletRequest request) {
        final Map<String, ?> logMessage = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.UNAUTHORIZED.value(),
                "error", HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "path", request.getServletPath(),
                "timestamp", LocalDateTime.now()
        );

        System.out.println("\nExceptionHandler_handleUnauthorizedAccessException:");
        System.out.println(logMessage + "\n");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of());
    }

    @ExceptionHandler(InvalidPaymentDataException.class)
    public ResponseEntity<?> handleInvalidPaymentDateException(InvalidPaymentDataException ex, HttpServletRequest request) {
        final Map<String, ?> response = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "path", request.getServletPath(),
                "timestamp", LocalDateTime.now()
        );

        System.out.println("\nExceptionHandler_handleInvalidPaymentDateException:");
        System.out.println(response +"\n");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        final Map<String, ?> response = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "path", request.getServletPath(),
                "timestamp", LocalDateTime.now()
        );

        System.out.println("\nExceptionHandler_handleNotFoundException:");
        System.out.println(response +"\n");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadAdminRequestException.class)
    public ResponseEntity<?> handleBadAdminRequestException(BadAdminRequestException ex, HttpServletRequest request) {
        final Map<String, ?> response = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "path", request.getServletPath(),
                "timestamp", LocalDateTime.now()
        );

        System.out.println("\nExceptionHandler_handleBadAdminRequestException:");
        System.out.println(response +"\n");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
