package io.github.dankoller.springaccountingservice.security;

import io.github.dankoller.springaccountingservice.service.AuditorService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final AuditorService auditorService;
    private final HttpServletRequest request;

    public AuthenticationFailureListener(AuditorService auditorService, HttpServletRequest request) {
        this.auditorService = auditorService;
        this.request = request;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        final Authentication authentication = event.getAuthentication();

        auditorService.registerLoginFailed(authentication.getName(), request.getServletPath());

        System.out.println("Bad credentials\non path:" +
                request.getServletPath() + "\nby user:" +
                authentication.getName());
    }
}
