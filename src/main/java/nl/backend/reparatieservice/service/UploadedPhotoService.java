package nl.backend.reparatieservice.service;

import nl.backend.reparatieservice.dto.UploadedPhotoDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.RepairRequest;
import nl.backend.reparatieservice.model.UploadedPhoto;
import nl.backend.reparatieservice.repository.RepairRequestRepository;
import nl.backend.reparatieservice.repository.UploadedPhotoRepository;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UploadedPhotoService {

    private final UploadedPhotoRepository uploadedPhotoRepository;
    private final RepairRequestRepository repairRequestRepository;


    public UploadedPhotoService(
            UploadedPhotoRepository uploadedPhotoRepository,
            RepairRequestRepository repairRequestRepository
    ) {
        this.uploadedPhotoRepository = uploadedPhotoRepository;
        this.repairRequestRepository = repairRequestRepository;
    }

    //upload photo
    public UploadedPhotoDto uploadPhoto(MultipartFile file) {
        try {

            String originalFilename = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;

            UploadedPhoto uploadedPhoto = new UploadedPhoto();
            uploadedPhoto.setFileName(uniqueFileName);
            uploadedPhoto.setUploadedPhotoUrl("https://repairservice/photos/" + uniqueFileName);
            uploadedPhoto.setPhotoData(file.getBytes());
            uploadedPhotoRepository.save(uploadedPhoto);

            UploadedPhotoDto uploadedPhotoDto = new UploadedPhotoDto();
            uploadedPhotoDto.id = uploadedPhoto.getId();
            uploadedPhotoDto.uploadedPhotoUrl = uploadedPhoto.getUploadedPhotoUrl();
            uploadedPhotoDto.fileName = uploadedPhoto.getFileName();
            return uploadedPhotoDto;

        } catch (IOException e) {
            throw new CustomException("Error uploading the photo", e);
        }
    }

    //photo by ID
    public byte[] getPhotoData(Long id) {
        try {
            UploadedPhoto uploadedPhoto = uploadedPhotoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("UploadedPhoto not found"));

            return uploadedPhoto.getPhotoData();
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve photo", e);
        }
    }

    public void deletePhoto(Long id) {
        try {
            UploadedPhoto uploadedPhoto = uploadedPhotoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("UploadedPhoto not found"));

            uploadedPhotoRepository.delete(uploadedPhoto);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete photo", e);
        }
    }

    // add photo to request
    @Transactional
    public UploadedPhotoDto addPhotoToRepairRequest(Long requestId, MultipartFile file) {
        try {
            RepairRequest repairRequest = repairRequestRepository.findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException("RepairRequest not found"));

            if (file.isEmpty()) {
                throw new IllegalArgumentException("Uploaded file is empty");
            }

            String originalFilename = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;

            UploadedPhoto uploadedPhoto = new UploadedPhoto();
            uploadedPhoto.setFileName(uniqueFileName);
            uploadedPhoto.setUploadedPhotoUrl("https://repairservice/photos/" + uniqueFileName);
            uploadedPhoto.setPhotoData(file.getBytes());
            uploadedPhoto.setRepairRequest(repairRequest);

            uploadedPhoto = uploadedPhotoRepository.save(uploadedPhoto);
            repairRequest.getUploadedPhotos().add(uploadedPhoto);
            repairRequestRepository.save(repairRequest);

            UploadedPhotoDto uploadedPhotoDto = new UploadedPhotoDto();
            uploadedPhotoDto.setId(uploadedPhoto.getId());
            uploadedPhotoDto.setFileName(uniqueFileName);
            uploadedPhotoDto.setUploadedPhotoUrl(uploadedPhoto.getUploadedPhotoUrl());

            return uploadedPhotoDto;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw new CustomException("Error uploading the photo", e);
        }
    }
}

