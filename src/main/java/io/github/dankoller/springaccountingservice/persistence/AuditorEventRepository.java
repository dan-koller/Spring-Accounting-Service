package io.github.dankoller.springaccountingservice.persistence;

import io.github.dankoller.springaccountingservice.entity.AuditorEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditorEventRepository extends CrudRepository<AuditorEvent, Long> {
    List<AuditorEvent> findAll();
}
