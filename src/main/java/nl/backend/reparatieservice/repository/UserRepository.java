package nl.backend.reparatieservice.repository;

import nl.backend.reparatieservice.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
