package io.github.dankoller.springaccountingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "User account is locked")
public class UserLockedException extends RuntimeException {
}
