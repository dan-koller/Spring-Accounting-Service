package io.github.dankoller.springaccountingservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.dankoller.springaccountingservice.entity.Role;
import io.github.dankoller.springaccountingservice.entity.User;

import java.util.Set;
import java.util.TreeSet;

public class UserDataResponse {

    // User data without sensitive information
    private final long id;
    private final String name;
    private final String lastname;
    private final String email;

    // Applied roles
    private final TreeSet<Role> roles;

    public UserDataResponse(long id, String name, String lastname, String email, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.roles = new TreeSet<>(roles);
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    @JsonProperty("lastname")
    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public static UserDataResponse fromUser(User user) {
        return new UserDataResponse(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getEmail(),
                user.getRoles()
        );
    }

    @Override
    public String toString() {
        return "UserDataResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
