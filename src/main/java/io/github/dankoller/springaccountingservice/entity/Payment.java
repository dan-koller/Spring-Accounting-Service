package io.github.dankoller.springaccountingservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

// Special class to allow payments to specific employees (users)
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Email(regexp = "[a-zA-Z0-9]+@acme.com")
    private String email;

    @NotBlank
    @Pattern(regexp = "^0[1-9]-\\d{4}$|^(1[012])-\\d{4}$")
    private String period;

    private long salary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user", nullable = false)
    @JsonBackReference
    private User user;

    public Payment(String period, long salary, User user) {
        this.period = period;
        this.salary = salary;
        this.user = user;
        this.email = user.getEmail();
    }

    public Payment() {
    }

    public Long getId() {
        return id;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public long getSalary() {
        return salary;
    }

    public User getUser() {
        return user;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
