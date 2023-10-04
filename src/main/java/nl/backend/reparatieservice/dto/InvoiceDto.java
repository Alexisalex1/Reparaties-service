package nl.backend.reparatieservice.dto;

import java.math.BigDecimal;

public class InvoiceDto {
    public Long id;
    public BigDecimal repairItemsCost;
    public BigDecimal repairOptionsCost;
    public BigDecimal shippingCost;
    public BigDecimal tax;
    public BigDecimal totalAmount;
}