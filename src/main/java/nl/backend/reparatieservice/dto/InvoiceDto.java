package nl.backend.reparatieservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class InvoiceDto {
    public Long id;

    @NotNull(message = "Repair items cost is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Repair items cost must be a non-negative value")
    public BigDecimal repairItemsCost;

    @NotNull(message = "Repair options cost is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Repair options cost must be a non-negative value")
    public BigDecimal repairOptionsCost;

    @NotNull(message = "Shipping cost is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Shipping cost must be a non-negative value")
    public BigDecimal shippingCost;

    @NotNull(message = "Tax is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Tax must be a non-negative value")
    public BigDecimal tax;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Total amount must be a non-negative value")
    public BigDecimal totalAmount;
}