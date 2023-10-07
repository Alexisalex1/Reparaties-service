package nl.backend.reparatieservice.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import nl.backend.reparatieservice.model.Invoice;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RepairRequestDto {
    public Long requestId;
    @NotBlank(message = "Repair description is required")
    @Size(max = 500, message = "Repair description must not exceed 500 characters")
    public String repairDescription;

    @NotNull(message = "Repair date is required")
    @FutureOrPresent(message = "Repair date must be in the present")
    public LocalDateTime repairDate;

    @NotNull(message = "Customer information is required")
    @Valid
    @JsonBackReference
    public CustomerDto customer;

    @NotNull(message = "Total cost is required")
    public BigDecimal totalCost;
    public Invoice invoice;

    public List<UploadedPhotoDto> uploadedPhotos;
    public RepairStatusDto repairStatus;

    public List<RepairItemDto> repairItems;

    public List<RepairOptionDto> repairOptions;


    public void setRepairOptions (List<RepairOptionDto> repairOptions) {
        this.repairOptions = repairOptions;
    }

    public void setRepairItems(List<RepairItemDto> repairItems) {
        this.repairItems = repairItems;
    }
    public void setUploadedPhotos(List<UploadedPhotoDto> uploadedPhotos) {
        this.uploadedPhotos = uploadedPhotos;
    }

}
