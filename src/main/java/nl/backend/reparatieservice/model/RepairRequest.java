package nl.backend.reparatieservice.model;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
public class RepairRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    private String repairDescription;
    private LocalDateTime repairDate;
    private BigDecimal totalCost;

    @OneToOne(mappedBy = "repairRequest",  cascade = CascadeType.ALL)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "repairRequest", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<UploadedPhoto> uploadedPhotos;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private RepairStatus repairStatus;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "repair_request_items",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<RepairItem> repairItems;

    @ManyToMany
    @JoinTable(
            name = "repair_request_options",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<RepairOption> repairOptions;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public List<UploadedPhoto> getUploadedPhotos() {
        return uploadedPhotos;
    }


    public List<RepairOption> getRepairOptions() {
        return repairOptions;
    }

    public Long getRequestId() {
        return requestId;
    }

    public String getRepairDescription() {
        return repairDescription;
    }

    public void setRepairDescription(String repairDescription) {
        this.repairDescription = repairDescription;
    }

    public LocalDateTime getRepairDate() {
        return repairDate;
    }

    public void setUploadedPhotos(List<UploadedPhoto> uploadedPhotos) {
        this.uploadedPhotos = uploadedPhotos;
    }

    public void setRepairDate(LocalDateTime repairDate) {
        this.repairDate = repairDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setRepairStatus(RepairStatus pendingStatus) {
        this.repairStatus = pendingStatus;
    }

    public List<RepairItem> getRepairItems() {
        return repairItems;
    }

    public RepairStatus getRepairStatus() { return repairStatus;
    }

    public void setUploadedPhoto(byte[] photoData) {
    }
}
