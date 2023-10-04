package nl.backend.reparatieservice.service;

import nl.backend.reparatieservice.dto.InvoiceDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.Invoice;
import nl.backend.reparatieservice.model.RepairRequest;
import nl.backend.reparatieservice.repository.InvoiceRepository;
import nl.backend.reparatieservice.repository.RepairRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final RepairRequestRepository repairRequestRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, RepairRequestRepository repairRequestRepository) {
        this.invoiceRepository = invoiceRepository;
        this.repairRequestRepository = repairRequestRepository;
    }

    //creates an invoice and attach it to a given repair request
    public InvoiceDto createInvoice(Long repairRequestId) {
        try {
            RepairRequest repairRequest = repairRequestRepository.findById(repairRequestId)
                    .orElseThrow(() -> new EntityNotFoundException("RepairRequest not found"));


            Invoice invoice = new Invoice();


            invoice.setRepairItemsCost(BigDecimal.ZERO);
            invoice.setRepairOptionsCost(BigDecimal.ZERO);
            invoice.setShippingCost(new BigDecimal("4.00"));
            invoice.setTax(BigDecimal.ZERO);
            invoice.setTotalAmount(invoice.getShippingCost());
            invoice.setRepairRequest(repairRequest);
            invoice = invoiceRepository.save(invoice);

            repairRequest.setInvoice(invoice);
            repairRequestRepository.save(repairRequest);

            InvoiceDto createdInvoiceDto = new InvoiceDto();
            createdInvoiceDto.id = invoice.getId();
            createdInvoiceDto.repairItemsCost = invoice.getRepairItemsCost();
            createdInvoiceDto.repairOptionsCost = invoice.getRepairOptionsCost();
            createdInvoiceDto.shippingCost = invoice.getShippingCost();
            createdInvoiceDto.tax = invoice.getTax();
            createdInvoiceDto.totalAmount = invoice.getTotalAmount();

            return createdInvoiceDto;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to create and attach invoice", e);
        }
    }
}