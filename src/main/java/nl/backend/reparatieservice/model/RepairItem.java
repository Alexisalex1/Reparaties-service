package nl.backend.reparatieservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;


import java.math.BigDecimal;
import java.util.List;

@Entity
public class RepairItem {
    @Id
    @GeneratedValue
    private Long repairItemId;
    private String itemName;
    private String description;
    private BigDecimal cost;

    @ManyToMany(mappedBy = "repairItems")
    private List<RepairRequest> repairRequests;


    public Long getId() {
        return repairItemId;
    }

    public void setId(Long id) {
        repairItemId = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
