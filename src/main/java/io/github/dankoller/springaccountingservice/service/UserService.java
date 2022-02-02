package io.github.dankoller.springaccountingservice.service;

import io.github.dankoller.springaccountingservice.entity.Role;
import io.github.dankoller.springaccountingservice.entity.User;
import io.github.dankoller.springaccountingservice.exception.*;
import io.github.dankoller.springaccountingservice.log.Logger;
import io.github.dankoller.springaccountingservice.persistence.UserRepository;
import io.github.dankoller.springaccountingservice.request.SignupRequest;
import io.github.dankoller.springaccountingservice.response.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static boolean hasSetAdmin = false;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserDontExistException::new);
    }

    // new signup method
    public User signup(SignupRequest request) {

        // Create a temp user and see if it already exists
        User tempUser = userRepository.findUserByEmail(request.getEmail());
        if (tempUser != null) {
            throw new UserExistsException();
        }

        // Validate password
        PasswordValidationService.validate(request.getPassword());

        // Set roles for new users
        Set<Role> userRoles;

        // Set first user as admin
        if (!hasSetAdmin && userRepository.count() == 0) userRoles = Set.of(Role.ADMINISTRATOR);
        // Set default user roles
        else userRoles = Set.of(Role.USER);

        // If application gets restarted and repository is not empty
        if (!hasSetAdmin) hasSetAdmin = true;

        // Create new user
        User user = new User(
                request.getName(),
                request.getLastname(),
                request.getEmail().toLowerCase(),
                request.getPassword(),
                userRoles
        );

        // Salt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user to db
        userRepository.save(user);

        return user;
    }

    @Transactional
    public Map<String, String> changePassword(String newPassword, String email) {
        // Validate minimum password requirements
        PasswordValidationService.validate(newPassword);

        final User user = userRepository.findUserByEmail(email);

        // Check if new password is the same as the old one
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new PasswordMatchesException();
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Return status
        return Map.of("email", user.getEmail(), "status", "The password has been updated successfully");
    }

    public ResponseEntity generateSalaryReport(User user, Optional<String> period) {
        // Logging path
        final String endpoint =
                "/api/empl/payment" + period.map(queryParam -> "?period=" + queryParam).orElse("");

        if (user.isLocked()) {
            throw new UserLockedException();
        }

        if (period.isPresent()) {
            if (!isValidDate(period.get())) {
                throw new BadDateException();
            }

            final Optional<PaymentResponse> response = user.getPayments().stream()
                    .filter(payment -> payment.getPeriod().equalsIgnoreCase(period.get()))
                    .map(payment -> new PaymentResponse(
                            user.getName(),
                            payment.getPeriod(),
                            user.getLastname(),
                            payment.getSalary()
                    )).findFirst();

            // Log events
            Logger.logResponse(HttpMethod.GET, endpoint, response);

            return ResponseEntity.ok(response);
        }

        final List<PaymentResponse> responseList = user.getPayments().stream()
                .map(payment -> new PaymentResponse(
                        user.getName(),
                        payment.getPeriod(),
                        user.getLastname(),
                        payment.getSalary()
                )).sorted(Comparator.comparing(PaymentResponse::getPeriodAsYearMonth).reversed())
                .collect(Collectors.toList());

        // Log events
        Logger.logResponse(HttpMethod.GET, endpoint, responseList);

        return ResponseEntity.ok(responseList);
    }

    private boolean isValidDate(String date) {
        try {
            YearMonth.parse(date, DateTimeFormatter.ofPattern("MM-yyyy"));
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

}