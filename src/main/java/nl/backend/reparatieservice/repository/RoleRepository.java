package nl.backend.reparatieservice.repository;
import nl.backend.reparatieservice.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, String> {
}
