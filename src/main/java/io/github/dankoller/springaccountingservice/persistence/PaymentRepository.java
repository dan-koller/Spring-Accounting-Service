package io.github.dankoller.springaccountingservice.persistence;

import io.github.dankoller.springaccountingservice.entity.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {

    Optional<Payment> findUserByEmailAndPeriod(String email, String period);

    boolean existsByUserEmailAndPeriod(String email, String period);

}
