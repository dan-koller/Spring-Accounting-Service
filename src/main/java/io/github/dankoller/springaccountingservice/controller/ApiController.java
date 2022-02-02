package io.github.dankoller.springaccountingservice.controller;

import io.github.dankoller.springaccountingservice.entity.User;
import io.github.dankoller.springaccountingservice.exception.InvalidPaymentDataException;
import io.github.dankoller.springaccountingservice.log.Logger;
import io.github.dankoller.springaccountingservice.request.PaymentRequest;
import io.github.dankoller.springaccountingservice.service.PaymentService;
import io.github.dankoller.springaccountingservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ApiController {

    @Autowired
    UserService userService;
    @Autowired
    PaymentService paymentService;

    // Upload payrolls
    @PostMapping(path = "api/acct/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadSalary(@Valid @RequestBody List<PaymentRequest> request) {
        // Log events
        final String endpoint = "api/acct/payments";
        Logger.logRequest(HttpMethod.POST, endpoint, request == null ? "null" : request);
        if (request == null) {
            throw new InvalidPaymentDataException("request should have a body");
        }

        paymentService.uploadSalary(request);

        var response = Map.of("status", "Added successfully!");

        Logger.logResponse(HttpMethod.POST, endpoint, response);

        return ResponseEntity.ok(response);
    }

    // Change salary of specific users
    @PutMapping(path = "api/acct/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeSalary(@Valid @RequestBody PaymentRequest request) {
        // Log events
        final String endpoint = "api/acct/payments";
        Logger.logRequest(HttpMethod.PUT, endpoint, request);

        paymentService.updateSalary(request);

        var response = Map.of("status", "Added successfully!");

        Logger.logResponse(HttpMethod.PUT, endpoint, response);

        return ResponseEntity.ok(response);
    }

    // Payroll of employees
    @GetMapping(value = "/api/empl/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Optional<String> period
    ) {
        // Log events
        final String endpoint =
                "/api/empl/payment" + period.map(queryParam -> "?period=" + queryParam).orElse("");
        Logger.logRequest(HttpMethod.GET, endpoint, userDetails.getUsername());

        // Find specified user
        User user = userService.findUserByEmail(userDetails.getUsername());

        // Generate salary report
        return userService.generateSalaryReport(user, period);
    }
}
