package io.github.dankoller.springaccountingservice.persistence;

import io.github.dankoller.springaccountingservice.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findUserByEmailIgnoreCase(String email);

    List<User> findAll();

    User findUserByEmail(String email);

    Optional<User> findByEmail(String email);
}