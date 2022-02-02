package io.github.dankoller.springaccountingservice.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static String path;
    private static String message;

    public static void setPath(String path) {
        RestAuthenticationEntryPoint.path = path;
    }

    public static void setMessage(String message) {
        RestAuthenticationEntryPoint.message = message;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        final Map<String, String> responseMap = Map.of(
                "message", message != null ? message : "Unauthorized!",
                "status", String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                "error", HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "path", path != null ? path: request.getServletPath(),
                "timestamp", LocalDateTime.now().toString()
        );

        final String responseString = responseMap.entrySet().stream()
                .map(e -> e.getKey().equals("status") ?
                        "\"status\": 401"
                        : String.format("\"%s\": \"%s\"", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ", "{ ", " }"));

        System.out.println("\nRestAuthenticationEntryPoint_commence");
        System.out.println(authException.getMessage());
        System.out.println(response +"\n");

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(responseString);
        response.setStatus(401);
    }
}
