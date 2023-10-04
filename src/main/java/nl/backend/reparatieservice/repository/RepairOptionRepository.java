package nl.backend.reparatieservice.repository;

import nl.backend.reparatieservice.model.RepairOption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RepairOptionRepository extends CrudRepository<RepairOption,Long> {
}
