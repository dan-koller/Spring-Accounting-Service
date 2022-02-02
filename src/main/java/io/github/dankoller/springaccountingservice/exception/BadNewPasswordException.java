package io.github.dankoller.springaccountingservice.exception;

public class BadNewPasswordException extends RuntimeException {
    public BadNewPasswordException(String message) {
        super(message);
    }
}
