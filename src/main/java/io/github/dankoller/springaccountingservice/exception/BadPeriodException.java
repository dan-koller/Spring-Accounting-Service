package io.github.dankoller.springaccountingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Select a different payment period!")
public class BadPeriodException extends RuntimeException{
}
