package io.github.dankoller.springaccountingservice.controller;

import io.github.dankoller.springaccountingservice.entity.User;
import io.github.dankoller.springaccountingservice.log.Logger;
import io.github.dankoller.springaccountingservice.request.SignupRequest;
import io.github.dankoller.springaccountingservice.response.UserDataResponse;
import io.github.dankoller.springaccountingservice.service.AuditorService;
import io.github.dankoller.springaccountingservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class RegistrationController {

    @Autowired
    UserService userService;

    @Autowired // If this doesn't work try creating a constructor
    AuditorService auditorService;

    // Signup
    @PostMapping(path = "/api/auth/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDataResponse> signup(@Valid @RequestBody SignupRequest request) {
        // Log events
        final String endpoint = "/api/auth/signup";
        Logger.logRequest(HttpMethod.POST, endpoint, request);

        // Check for blank values on signup in user class annotations
        User newUser = userService.signup(request);
        UserDataResponse userDataResponse = UserDataResponse.fromUser(newUser);

        // Log events
        Logger.logResponse(HttpMethod.POST, endpoint, userDataResponse);
        auditorService.registerUserCreatedEvent(newUser.getEmail());

        return ResponseEntity.ok(userDataResponse);
    }

    // Change password
    @PostMapping(path = "/api/auth/changepass", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePass(
            @RequestBody Map<String, String> requestMap,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Log events
        final String endpoint = "/api/auth/changepass";
        Logger.logRequest(HttpMethod.POST, endpoint, userDetails.getUsername());

        Map<String, ?> response = userService.changePassword(
                requestMap.get("new_password"), userDetails.getUsername()
        );

        // Log events
        Logger.logResponse(HttpMethod.POST, endpoint, response);
        auditorService.registerPasswordChange(userDetails.getUsername());

        // Change password
        return ResponseEntity.ok(response);
    }

}