package nl.backend.reparatieservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RepairStatusDto {
    public Long Id;

    @NotBlank(message = "Status name is required")
    @Size(min = 2, max = 50, message = "Status name must be between 2 and 50 characters")
    public String statusName;
}