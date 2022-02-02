package io.github.dankoller.springaccountingservice.service;

import io.github.dankoller.springaccountingservice.entity.Payment;
import io.github.dankoller.springaccountingservice.entity.User;
import io.github.dankoller.springaccountingservice.exception.BadSalaryException;
import io.github.dankoller.springaccountingservice.persistence.PaymentRepository;
import io.github.dankoller.springaccountingservice.persistence.UserRepository;
import io.github.dankoller.springaccountingservice.request.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PaymentService(UserRepository userRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    public void uploadSalary(List<PaymentRequest> request) {
        // Validate request
        for (PaymentRequest payment : request) {
            // Find specified user
            final User user = userRepository.findUserByEmail(payment.getEmail());

            PaymentValidatorService.isValidPayment(payment, userRepository, paymentRepository, false);
        }

        final List<Payment> paymentList = request.stream()
                .map(this::paymentFactory)
                .collect(Collectors.toList());

        savePayment(paymentList);
    }

    public void updateSalary(PaymentRequest request) {

        // Find specified user
        final User user = userRepository.findUserByEmail(request.getEmail());

        // Validate requested payment
        PaymentValidatorService.isValidPayment(request, userRepository, paymentRepository, true);

        final Optional<Payment> maybePayment =
                paymentRepository.findUserByEmailAndPeriod(request.getEmail(), request.getPeriod());

        System.out.println(maybePayment.isPresent());

        if(maybePayment.isPresent()) {
            final Payment payment = maybePayment.get();
            payment.setSalary(request.getSalary());
            paymentRepository.save(payment);
        } else {
            throw new BadSalaryException();
        }
    }

    @Transactional
    private void savePayment(List<Payment> payments) {
        paymentRepository.saveAll(payments);
    }

    private Payment paymentFactory(PaymentRequest request) {
        return new Payment(
                request.getPeriod(),
                request.getSalary(),
                userRepository.findUserByEmail(request.getEmail())
        );
    }
}
