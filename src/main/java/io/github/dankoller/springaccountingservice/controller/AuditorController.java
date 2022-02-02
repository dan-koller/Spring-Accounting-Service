package io.github.dankoller.springaccountingservice.controller;

import io.github.dankoller.springaccountingservice.service.AuditorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditorController {

    private final AuditorService auditorService;

    public AuditorController(AuditorService auditorService) {
        this.auditorService = auditorService;
    }

    @GetMapping("/api/security/events")
    public ResponseEntity<?> getSecurityEvents() {
        return ResponseEntity.ok(auditorService.getAuditorEvents());
    }
}
