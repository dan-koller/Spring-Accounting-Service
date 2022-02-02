package io.github.dankoller.springaccountingservice.service;

import io.github.dankoller.springaccountingservice.entity.User;
import io.github.dankoller.springaccountingservice.exception.BadPeriodException;
import io.github.dankoller.springaccountingservice.exception.BadSalaryException;
import io.github.dankoller.springaccountingservice.exception.UserDontExistException;
import io.github.dankoller.springaccountingservice.persistence.PaymentRepository;
import io.github.dankoller.springaccountingservice.persistence.UserRepository;
import io.github.dankoller.springaccountingservice.request.PaymentRequest;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class PaymentValidatorService {

    public static void isValidPayment(PaymentRequest paymentRequest, UserRepository userRepository, PaymentRepository paymentRepository, boolean isUpdated) {

        final boolean isValidSalary = paymentRequest.getSalary() >= 0;
        if (!isValidSalary) {
            throw new BadSalaryException();
        }

        final boolean isValidDate = isValidDate(paymentRequest.getPeriod());
        if (!isValidDate) {
            throw new BadPeriodException();
        }

        final User user = userRepository.findUserByEmail(paymentRequest.getEmail());
        if (user == null) {
            throw new UserDontExistException();
        }

        if(!isUpdated) {
            final boolean existPayment = paymentRepository.existsByUserEmailAndPeriod(paymentRequest.getEmail(), paymentRequest.getPeriod());
            if(existPayment) {
                throw new BadPeriodException();
            }
        }
    }

    private static boolean isValidDate(String date) {
        try{
            YearMonth.parse(date, DateTimeFormatter.ofPattern("MM-yyyy"));
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
