package nl.backend.reparatieservice.repository;

import nl.backend.reparatieservice.model.RepairRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface RepairRequestRepository extends CrudRepository<RepairRequest,Long> {
    Optional<RepairRequest> findByRequestId(@Param("requestId") Long requestId);

    List<RepairRequest> findByCustomerCustomerId(Long customerId);
}
