package nl.backend.reparatieservice.dto;


public class UploadedPhotoDto {
    public Long id;

    public String fileName;
    public String uploadedPhotoUrl;


    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUploadedPhotoUrl(String uploadedPhotoUrl) {
        this.uploadedPhotoUrl = uploadedPhotoUrl;
    }
}
