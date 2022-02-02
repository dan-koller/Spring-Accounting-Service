package io.github.dankoller.springaccountingservice.controller;

import io.github.dankoller.springaccountingservice.exception.BadAdminRequestException;
import io.github.dankoller.springaccountingservice.log.Logger;
import io.github.dankoller.springaccountingservice.service.AdminService;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

// @RequestMapping("/api/admin")
@RestController
public class AdministrationController {

    private AdminService adminService;

    public AdministrationController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/api/admin/user")
    public ResponseEntity<?> getUsersInfo() {
        // Log events
        final String endpoint = "/api/admin/user";
        Logger.logRequest(HttpMethod.GET, endpoint);

        List<?> users = adminService.getUsers(); // May be final

        Logger.logResponse(HttpMethod.GET, endpoint, users);

        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/api/admin/user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
        // Log events
        final String endpoint = "/api/admin/user/" + email.toLowerCase();
        Logger.logRequest(HttpMethod.DELETE, endpoint);


        adminService.deleteUser(email);
        Map<String, String> deletedUser = Map.of("user", email, "status", "Deleted successfully!");

        Logger.logResponse(HttpMethod.DELETE, endpoint, deletedUser);

        return ResponseEntity.ok(deletedUser);
    }

    @DeleteMapping("/api/admin/user")
    public ResponseEntity<?> badDeleteRequest() {
        throw new BadAdminRequestException("Please specify the user you want to delete!");
    }

    @PutMapping("/api/admin/user/role")
    public ResponseEntity<?> setRole(@RequestBody Map<String, String> request) {
        // Log events
        final String endpoint = "/api/admin/user/role";
        Logger.logRequest(HttpMethod.PUT, endpoint, request);

        var response = adminService.updateRoles(request);

        Logger.logResponse(HttpMethod.PUT, endpoint, response);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/admin/user/access")
    public ResponseEntity<?> setAccess(@RequestBody Map<String, String> request,
                                       @AuthenticationPrincipal Principal principal) {
        // Log events
        final String endpoint = "/api/admin/user/access";
        Logger.logRequest(HttpMethod.PUT, endpoint, request);

        var response = adminService.updateAccess(request, principal.getName());

        Logger.logResponse(HttpMethod.PUT, endpoint, response);

        return ResponseEntity.ok(response);
    }

}
