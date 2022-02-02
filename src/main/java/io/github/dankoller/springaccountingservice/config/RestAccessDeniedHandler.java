package io.github.dankoller.springaccountingservice.config;

import io.github.dankoller.springaccountingservice.auth.WebSecurityConfigurerImpl;
import io.github.dankoller.springaccountingservice.service.AuditorService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final AuditorService auditorService;

    public RestAccessDeniedHandler(AuditorService auditorService) {
        this.auditorService = auditorService;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        final String path = request.getServletPath();
        final String email = WebSecurityConfigurerImpl.getAuthentication().getName();

        final Map<String, ?> toLog = Map.of(
                "message", "Access Denied!",
                "status", HttpStatus.FORBIDDEN.value(),
                "error", HttpStatus.FORBIDDEN.getReasonPhrase(),
                "path", request.getServletPath(),
                "timestamp", LocalDateTime.now()
        );

        System.out.println("\nRestAccessDeniedHandler_handle");
        System.out.println(toLog + "\n");

        auditorService.registerAccessDenied(email, path);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
    }
}
