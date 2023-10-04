package nl.backend.reparatieservice.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class RepairOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;
    private String optionName;
    private String description;
    private BigDecimal cost;

    @ManyToMany(mappedBy = "repairOptions")
    private List<RepairRequest> repairRequests;


    public RepairOption() {
    }

    public Long getId() {
        return optionId;
    }

    public void setId(Long id) {
        optionId = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
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
