package nl.backend.reparatieservice.controller;

import nl.backend.reparatieservice.dto.InvoiceDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    //create invoice and attach to repairRequest
    @PostMapping("/attach-to-request/{repairRequestId}")
    public ResponseEntity<?> createAndAttachInvoiceToRepairRequest(
            @PathVariable Long repairRequestId
    ) {
        try {
            InvoiceDto createdInvoice = invoiceService.createInvoice(repairRequestId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Repair request not found: " + e.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create and attach invoice: " + e.getMessage());
        }
    }
}