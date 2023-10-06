package nl.backend.reparatieservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class RepairOptionDto {
    public Long id;

    @NotBlank(message = "Option name is required")
    @Size(min = 2, max = 100, message = "Option name must be between 2 and 100 characters")
    public String optionName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    public String description;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Cost must be a non-negative value")
    public BigDecimal cost;
}