package io.github.dankoller.springaccountingservice.exception;

public class BadAdminRequestException extends RuntimeException{
    public BadAdminRequestException(String message) {
        super(message);
    }
}
