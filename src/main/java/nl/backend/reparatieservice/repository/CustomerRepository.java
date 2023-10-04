package nl.backend.reparatieservice.repository;

import nl.backend.reparatieservice.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CustomerRepository extends CrudRepository <Customer, Long> {
}
