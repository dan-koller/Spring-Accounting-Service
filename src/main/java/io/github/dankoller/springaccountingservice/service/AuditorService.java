package io.github.dankoller.springaccountingservice.service;

import io.github.dankoller.springaccountingservice.entity.AuditorEvent;
import io.github.dankoller.springaccountingservice.entity.Role;
import io.github.dankoller.springaccountingservice.entity.User;
import io.github.dankoller.springaccountingservice.persistence.AuditorEventRepository;
import io.github.dankoller.springaccountingservice.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuditorService {

    private final AuditorEventRepository auditorEventRepository;
    private final UserRepository userRepository;

    public AuditorService(AuditorEventRepository auditorEventRepository, UserRepository userRepository) {
        this.auditorEventRepository = auditorEventRepository;
        this.userRepository = userRepository;
    }

    public List<?> getAuditorEvents() {
        return auditorEventRepository.findAll();
    }

    public void registerUserCreatedEvent(String email) {

        final AuditorEvent auditorEvent = new AuditorEvent(
                /*action*/ "CREATE_USER",
                /*subject*/ "Anonymous",
                /*object*/ email,
                /*path*/ "/api/auth/signup"
        );
        auditorEventRepository.save(auditorEvent);
    }

    public void registerGrantRoleEvent(Role role, String userEmail, String adminEmail) {

        final AuditorEvent auditorEvent = new AuditorEvent(
                /*action*/ "GRANT_ROLE",
                /*subject*/ adminEmail,
                /*object*/ String.format("Grant role %s to %s", role.name(), userEmail),
                /*path*/ "/api/admin/user/role"
        );
        auditorEventRepository.save(auditorEvent);
    }

    public void registerLoginFailed(String email, String path){
        System.out.println("Login failed for " + email);
        final AuditorEvent loginFailedAuditorEvent = new AuditorEvent(
                /*action*/"LOGIN_FAILED",
                /*subject*/ email,
                /*object*/ path,
                /*path*/ path
        );

        final Optional<User> tempUser = userRepository.findUserByEmailIgnoreCase(email);

        boolean isBruteForce = false;
        if (tempUser.isPresent()){
            final User user = tempUser.get();
            final boolean wasNotLocked = !user.isLocked();
            user.incrementFailedLoginAttempt();
            System.out.println("login failed attempts " + user.getFailedLoginAttempts());
            if (wasNotLocked && user.isLocked()) {
                isBruteForce = true;
            }
            userRepository.save(user);
        }

        auditorEventRepository.save(loginFailedAuditorEvent);
        if (isBruteForce) {
            registerBruteForce(email, path);
            registerLockUserEvent(email, path);
        }
    }

    private void registerBruteForce(String email, String path) {
        System.out.println("Brute force event triggered by user " + email);
        final AuditorEvent loginFailedAuditorEvent = new AuditorEvent(
                /*action*/"BRUTE_FORCE",
                /*subject*/ email,
                /*object*/ path,
                /*path*/ path
        );
        auditorEventRepository.save(loginFailedAuditorEvent);
    }

    public void registerRemoveRoleEvent(Role role, String userEmail, String adminEmail) {
        final AuditorEvent auditorEvent = new AuditorEvent(
                /*action*/ "REMOVE_ROLE",
                /*subject*/ adminEmail,
                /*object*/ String.format("Remove role %s from %s", role.name(), userEmail),
                /*path*/ "/api/admin/user/role"
        );
        auditorEventRepository.save(auditorEvent);
    }

    public void registerRemoveUser(String userEmail, String adminEmail){
        final AuditorEvent auditorEvent = new AuditorEvent(
                /*action*/ "DELETE_USER",
                /*subject*/ adminEmail,
                /*object*/ userEmail,
                /*path*/ "/api/admin/user"
        );
        auditorEventRepository.save(auditorEvent);
    }

    public void registerPasswordChange(String userEmail) {
        final AuditorEvent auditorEvent = new AuditorEvent(
                /*action*/ "CHANGE_PASSWORD",
                /*subject*/ userEmail,
                /*object*/ userEmail,
                /*path*/ "/api/auth/changepass"
        );
        auditorEventRepository.save(auditorEvent);
    }

    public void registerAccessDenied(String userEmail, String path) {
        final AuditorEvent auditorEvent = new AuditorEvent(
                /*action*/ "ACCESS_DENIED",
                /*subject*/ userEmail,
                /*object*/ path,
                /*path*/ path
        );
        auditorEventRepository.save(auditorEvent);
    }

    public void registerLockUserEvent(String userEmail, String path) {
        System.out.println("User locked " + userEmail);
        final AuditorEvent auditorEvent = new AuditorEvent(
                /*action*/ "LOCK_USER",
                /*subject*/ userEmail,
                /*object*/ "Lock user " + userEmail,
                /*path*/ path
        );
        auditorEventRepository.save(auditorEvent);
    }

    public void registerUnlockUserEvent(String userEmail, String admin, String path) {
        System.out.println("User unlocked " + userEmail);

        final AuditorEvent auditorEvent = new AuditorEvent(
                /*action*/ "UNLOCK_USER",
                /*subject*/ admin,
                /*object*/ "Unlock user " + userEmail,
                /*path*/ path
        );
        auditorEventRepository.save(auditorEvent);
    }

}
