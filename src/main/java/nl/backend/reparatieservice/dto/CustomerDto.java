package nl.backend.reparatieservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CustomerDto {
    public Long customerId;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    public String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be a 10-digit number")
    public String phoneNumber;
    @JsonIgnore

    public List<RepairRequestDto> repairRequests;

    public void setRepairRequests(List<RepairRequestDto> repairRequests) {
        this.repairRequests = repairRequests;
    }
}
