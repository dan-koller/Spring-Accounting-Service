package io.github.dankoller.springaccountingservice.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.dankoller.springaccountingservice.auth.WebSecurityConfigurerImpl;
import io.github.dankoller.springaccountingservice.entity.Role;
import io.github.dankoller.springaccountingservice.entity.User;
import io.github.dankoller.springaccountingservice.service.PasswordValidationService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class SignupRequest {

    private final PasswordEncoder encoder = WebSecurityConfigurerImpl.getEncoder();

    @NotEmpty
    private final String name;

    @NotEmpty
    private final String lastname;

    @NotEmpty
    @Email(regexp = "[a-zA-Z0-9]+@acme.com")
    private String email;

    private final String password;

    @JsonCreator
    public SignupRequest(@JsonProperty("name")String name,
                         @JsonProperty("lastname") String lastname,
                         @JsonProperty("email") String email,
                         @JsonProperty("password") String password) {

        this.name = name;
        this.lastname = lastname;
        this.email = email != null ? email.toLowerCase() : null;
        this.password = password;
    }

    // Explicitly assign role to user and return that user
    public User toUser(String password) {
        PasswordValidationService.validate(password);
        return new User(name, lastname, email, encoder.encode(password), Set.of(Role.USER));
    }

    public User toAdmin(String password) {
        PasswordValidationService.validate(password);
        return new User(name, lastname, email, encoder.encode(password), Set.of(Role.ADMINISTRATOR));
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    // May not work
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "encoder=" + encoder +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
