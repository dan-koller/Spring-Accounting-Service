package io.github.dankoller.springaccountingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "No such role found!")
public class RoleDontExistException extends RuntimeException {
}
