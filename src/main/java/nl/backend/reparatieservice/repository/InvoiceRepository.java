package nl.backend.reparatieservice.repository;

import nl.backend.reparatieservice.model.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
}
