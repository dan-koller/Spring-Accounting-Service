package io.github.dankoller.springaccountingservice.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRequest {

    final String email;
    final String period;
    final long salary;

    @JsonCreator
    public PaymentRequest(@JsonProperty("employee") String email,
                          @JsonProperty("period") String period,
                          @JsonProperty("salary") long salary) {
        this.email = email;
        this.period = period;
        this.salary = salary;
    }

    public String getPeriod() {
        return period;
    }

    public String getEmail() {
        return email;
    }

    public long getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "email='" + email + '\'' +
                ", period='" + period + '\'' +
                ", salary=" + salary +
                '}';
    }
}
