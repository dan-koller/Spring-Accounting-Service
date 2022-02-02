package io.github.dankoller.springaccountingservice.service;

import io.github.dankoller.springaccountingservice.auth.WebSecurityConfigurerImpl;
import io.github.dankoller.springaccountingservice.entity.Role;
import io.github.dankoller.springaccountingservice.entity.User;
import io.github.dankoller.springaccountingservice.exception.*;
import io.github.dankoller.springaccountingservice.persistence.UserRepository;
import io.github.dankoller.springaccountingservice.response.UserDataResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private UserRepository userRepository;
    private final AuditorService auditorService;

    public AdminService(UserRepository userRepository, AuditorService auditorService) {
        this.userRepository = userRepository;
        this.auditorService = auditorService;
    }

    // Returns an ordered List of all users in database
    public List<?> getUsers() {
        return userRepository.findAll().stream()
                .map(UserDataResponse::fromUser)
                .collect(Collectors.toList());
    }

    public void deleteUser(String email) {

        final Optional<User> tempUser = userRepository.findByEmail(email);

        if (tempUser.isEmpty()) {
            throw new UserDontExistException();
        } else {
            User deleteUser = tempUser.get();
            if (Role.isAdministrative(deleteUser.getRoles())) {
                throw new RemoveAdminException();
            } else {
                // Log events
                String adminEmail = WebSecurityConfigurerImpl.getAuthentication().getName();
                auditorService.registerRemoveUser(email, adminEmail);

                userRepository.delete(deleteUser);
            }
        }

    }

    public UserDataResponse updateRoles(Map<String, String> request) {

        Optional<Role> tempRoleRequest = Role.fromString("ROLE_" + request.getOrDefault("role", ""));

        if (tempRoleRequest.isEmpty()) {
            throw new BadRoleException();
        }

        Role requestRole = tempRoleRequest.get();

        String requestEmail = request.getOrDefault("user", "not found").toLowerCase();
        String requestOperation = request.getOrDefault("operation", "not found");

        Optional<User> tempUser = userRepository.findByEmail(requestEmail);

        if (tempUser.isEmpty()) {
            throw new UserDontExistException();
        }

        User user = tempUser.get();

        Set<Role> userRoles = user.getRoles();

        if (requestOperation.equals("GRANT")) {
            boolean hasRoleConflict = Role.isAdministrative(requestRole) && Role.isBusiness(userRoles)
                    || Role.isBusiness(requestRole) && Role.isAdministrative(userRoles);

            if (hasRoleConflict) {
                throw new BadRoleCombinationException();
            }

            userRoles.add(requestRole);
            User saveUser = userRepository.save(user);

            // Log events
            String adminEmail = WebSecurityConfigurerImpl.getAuthentication().getName();
            auditorService.registerGrantRoleEvent(requestRole, saveUser.getEmail(), adminEmail);

            return UserDataResponse.fromUser(saveUser);
        } else if (requestOperation.equals("REMOVE")) {
            if (Role.isAdministrative(requestRole) && Role.isAdministrative(userRoles)) {
                throw new RemoveAdminException();
            } else if (!userRoles.contains(requestRole)){
                throw new BadAdminRequestException("The user does not have a role!");
            } else if (userRoles.size() == 1) {
                throw new BadAdminRequestException("The user must have at least one role!");
            }

            userRoles.remove(requestRole);
            User saveUser = userRepository.save(user);

            // Log events
            String adminEmail = WebSecurityConfigurerImpl.getAuthentication().getName();
            auditorService.registerRemoveRoleEvent(requestRole, saveUser.getEmail(), adminEmail);

            return UserDataResponse.fromUser(saveUser);
        } else {
            throw new BadAdminRequestException("Invalid operation requested");
        }
    }

    public Map<String, ?> updateAccess(Map<String, String> request, String adminEmail) {

        final String operation = request.getOrDefault("operation", "");
        final String email = request.getOrDefault("user", "").toLowerCase();
        final Optional<User> tempUser = userRepository.findUserByEmailIgnoreCase(email);

        if (operation.equalsIgnoreCase("LOCK")) {
            if (tempUser.isPresent()) {
                final User user = tempUser.get();
                if (Role.isAdministrative(user.getRoles())) {
                    throw new BadAdminRequestException("Can't lock the ADMINISTRATOR!");
                }
                user.lock();
                userRepository.save(user);
            }
            return Map.of("status", "User " + email + " locked!");
        } else if (operation.equalsIgnoreCase("UNLOCK")) {
            if (tempUser.isPresent()) {
                final User user = tempUser.get();
                user.unlock();
                user.clearFailedLogins();
                userRepository.save(user);
            }
            auditorService.registerUnlockUserEvent(email, adminEmail, "api/admin/user/access");
            return Map.of("status", "User " + email + " unlocked!");
        } else {
            throw new BadAdminRequestException("Invalid operation requested!");
        }

    }
}
