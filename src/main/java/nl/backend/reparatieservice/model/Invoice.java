package nl.backend.reparatieservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Invoice
 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "request_id")
    private RepairRequest repairRequest;


    private BigDecimal repairItemsCost;
    private BigDecimal repairOptionsCost;
    private BigDecimal shippingCost;
    private BigDecimal tax;
    private BigDecimal totalAmount;


    public RepairRequest getRepairRequest() {
       return repairRequest;
    }

    public void setRepairRequest(RepairRequest repairRequest) {
       this.repairRequest = repairRequest;
    }

    public BigDecimal getRepairItemsCost() {
       return repairItemsCost;
    }

    public void setRepairItemsCost(BigDecimal repairItemsCost) {
       this.repairItemsCost = repairItemsCost;
    }

    public BigDecimal getRepairOptionsCost() {
       return repairOptionsCost;
    }

    public void setRepairOptionsCost(BigDecimal repairOptionsCost) {
       this.repairOptionsCost = repairOptionsCost;
    }

    public BigDecimal getShippingCost() {
       return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
       this.shippingCost = shippingCost;
    }

    public BigDecimal getTax() {
       return tax;
    }

    public void setTax(BigDecimal tax) {
       this.tax = tax;
    }

    public BigDecimal getTotalAmount() {
       return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
       this.totalAmount = totalAmount;
    }

    public Long getId() {
       return id;
    }

    public void setId(Long id) {
       this.id = id;
    }
 }
