package io.github.dankoller.springaccountingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Salary must be non-negative!")
public class BadSalaryException extends RuntimeException {
}
