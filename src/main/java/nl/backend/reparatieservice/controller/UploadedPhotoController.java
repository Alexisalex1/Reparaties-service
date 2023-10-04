package nl.backend.reparatieservice.controller;

import nl.backend.reparatieservice.dto.UploadedPhotoDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.service.UploadedPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploaded-photos")
public class UploadedPhotoController {

    private final UploadedPhotoService uploadedPhotoService;

    @Autowired
    public UploadedPhotoController(
            UploadedPhotoService uploadedPhotoService
    ) {
        this.uploadedPhotoService = uploadedPhotoService;

    }

    // upload a photo if repair request is not created
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhoto(@RequestPart("file") MultipartFile file) {
        try {
            UploadedPhotoDto uploadedPhotoDto = uploadedPhotoService.uploadPhoto(file);
            return new ResponseEntity<>(uploadedPhotoDto, HttpStatus.CREATED);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload photo: " + e.getMessage());
        }
    }

    // retrieve a photo by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable Long id) {
        try {
            byte[] photoData = uploadedPhotoService.getPhotoData(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type based on your file type
            return new ResponseEntity<>(photoData, headers, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Photo not found: " + e.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve photo: " + e.getMessage());
        }
    }

    // delete a photo by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        try {
            uploadedPhotoService.deletePhoto(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Photo not found: " + e.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete photo: " + e.getMessage());
        }
    }

    // upload and attach photo to repairRequest
    @PostMapping("/add-to-request/{requestId}")
    public ResponseEntity<?> addPhotoToRepairRequest(
            @PathVariable Long requestId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            UploadedPhotoDto uploadedPhotoDto = uploadedPhotoService.addPhotoToRepairRequest(requestId, file);
            return new ResponseEntity<>(uploadedPhotoDto, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Repair request not found: " + e.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload and attach photo: " + e.getMessage());
        }
    }
}
