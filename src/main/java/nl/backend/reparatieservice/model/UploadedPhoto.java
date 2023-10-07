package nl.backend.reparatieservice.model;

import jakarta.persistence.*;



@Entity
public class UploadedPhoto {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String uploadedPhotoUrl;
    @Lob
    private byte[] photoData;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)


    private RepairRequest repairRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }

    public RepairRequest getRepairRequest() {
        return repairRequest;
    }

    public void setRepairRequest(RepairRequest repairRequest) {
        this.repairRequest = repairRequest;
    }

    public String getUploadedPhotoUrl() {
        return uploadedPhotoUrl;
    }

    public void setUploadedPhotoUrl(String uploadedPhotoUrl) {
        this.uploadedPhotoUrl = uploadedPhotoUrl;
    }
}

