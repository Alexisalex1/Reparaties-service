package nl.backend.reparatieservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UploadedPhotoDto {
    public Long id;
    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name must not exceed 255 characters")
    public String fileName;
    @NotBlank(message = "Uploaded photo URL is required")
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
