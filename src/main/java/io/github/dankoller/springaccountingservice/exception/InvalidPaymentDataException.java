package io.github.dankoller.springaccountingservice.exception;

public class InvalidPaymentDataException extends RuntimeException {
    public InvalidPaymentDataException(String message) {
        super(message);
    }
}
