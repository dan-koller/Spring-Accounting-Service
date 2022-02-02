package io.github.dankoller.springaccountingservice.exception;

public class UniqueConstraintException extends RuntimeException {

    public UniqueConstraintException(String message) {
        super(message);
    }
}
