package nl.backend.reparatieservice.repository;

import nl.backend.reparatieservice.model.RepairStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairStatusRepository extends CrudRepository<RepairStatus,Long> {
    Optional<RepairStatus> findByStatusName(String statusName);
}
