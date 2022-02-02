package io.github.dankoller.springaccountingservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "User")
public class User {
    // Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotEmpty
    private String name;

    @Column
    @NotEmpty
    private String lastname;

    @Column
    @NotEmpty
    @Email(regexp = "[a-zA-Z0-9]+@acme.com")
    private String email;

    @Column
    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 12) // Passwords need to have at least 12 characters
    private String password;

    @JsonIgnore
    private String role;

    @Column
    @JsonIgnore
    @Pattern(regexp = "^0[1-9]-\\d{4}$|^(1[012])-\\d{4}$")
    private String period;

    @Column
    @JsonIgnore
    private long salary;

    // Security mechanismns
    private int failedLoginAttempts = 0;
    private boolean isLocked = false;

    // Map existing payments
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> payments;

    @ElementCollection(targetClass= Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="roles")
    @Column(name="role")
    Set<Role> roles;

    // Constructor for setting roles
    public User(String name, String lastname, String email, String password, Set<Role> roles) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // Default constructor
    public User() {}

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public void lock() {
        isLocked = true;
    }

    public void unlock() {
        isLocked = false;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void incrementFailedLoginAttempt() {
        failedLoginAttempts++;
        if (failedLoginAttempts > 5 && !Role.isAdministrative(roles)) {
            isLocked = true;
        }
    }

    public void clearFailedLogins() {
        failedLoginAttempts = 0;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}