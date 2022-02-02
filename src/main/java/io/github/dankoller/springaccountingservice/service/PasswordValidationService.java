package io.github.dankoller.springaccountingservice.service;

import io.github.dankoller.springaccountingservice.exception.NoPasswordException;
import io.github.dankoller.springaccountingservice.exception.PasswordBlacklistedException;
import io.github.dankoller.springaccountingservice.exception.PasswordTooShortException;
import io.github.dankoller.springaccountingservice.misc.PasswordBlacklist;
import org.springframework.stereotype.Service;

@Service
public class PasswordValidationService {

    public static void validate(String password) {

        if (password == null) {
            throw new NoPasswordException();
        }

        // Passwords need to have at least 12 characters
        if (password.length() < 12) {
            throw new PasswordTooShortException();
        }

        // Check if password is blacklisted
        if (PasswordBlacklist.blacklist.contains(password)) {
            throw new PasswordBlacklistedException();
        }
    }
}
