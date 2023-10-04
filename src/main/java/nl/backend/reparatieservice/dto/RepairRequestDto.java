package nl.backend.reparatieservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Lob;
import nl.backend.reparatieservice.model.Invoice;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RepairRequestDto {
    public Long requestId;
    public String repairDescription;
    public LocalDateTime repairDate;
    public CustomerDto customer;
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
