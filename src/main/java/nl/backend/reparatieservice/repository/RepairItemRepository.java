package nl.backend.reparatieservice.repository;

import nl.backend.reparatieservice.model.RepairItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RepairItemRepository extends CrudRepository<RepairItem,Long> {
}
