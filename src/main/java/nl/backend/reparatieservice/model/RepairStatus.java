package nl.backend.reparatieservice.model;

import jakarta.persistence.*;
import jdk.jfr.Enabled;

import java.util.List;

@Entity
public class RepairStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusId;
    private String statusName;

    @OneToMany(mappedBy = "repairStatus")
    private List<RepairRequest> repairRequests;

    public RepairStatus() {
    }

    public RepairStatus(String statusName){
        this.statusName = statusName;
    }

    public Long getId() {
        return statusId;
    }

    public void setId(Long id) {
        this.statusId = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
