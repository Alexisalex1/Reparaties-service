package nl.backend.reparatieservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class CustomerDto {
    public Long customerId;
    public String name;
    public String email;
    public String phoneNumber;

    @JsonIgnore
    public List<RepairRequestDto> repairRequests;

    public void setRepairRequests(List<RepairRequestDto> repairRequests) {
        this.repairRequests = repairRequests;
    }
}
