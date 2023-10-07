package nl.backend.reparatieservice.service;

import nl.backend.reparatieservice.dto.RepairStatusDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.RepairStatus;
import nl.backend.reparatieservice.repository.RepairStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepairStatusService {

    private final RepairStatusRepository repairStatusRepository;


    public RepairStatusService(RepairStatusRepository repairStatusRepository) {
        this.repairStatusRepository = repairStatusRepository;
    }

    private RepairStatusDto convertToRepairStatusDto(RepairStatus repairStatus) {
        RepairStatusDto repairStatusDto = new RepairStatusDto();
        repairStatusDto.Id =repairStatus.getId();
        repairStatusDto.statusName = repairStatus.getStatusName();

        return repairStatusDto;
    }

    // Create a new repair status
    public RepairStatus createRepairStatus(RepairStatusDto repairStatusDto) {
        try {
            RepairStatus repairStatus = new RepairStatus();
            repairStatus.setStatusName(repairStatusDto.statusName);

            return repairStatusRepository.save(repairStatus);
        } catch (Exception e) {

            throw new CustomException("Failed to create repair status", e);
        }
    }

    // Retrieve all repair statuses
    public List<RepairStatusDto> getAllRepairStatuses() {
        try {
            List<RepairStatus> repairStatuses = (List<RepairStatus>) repairStatusRepository.findAll();
            return repairStatuses
                    .stream()
                    .map(this::convertToRepairStatusDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve repair statuses", e);
        }
    }

    // Find repair status by ID
    public RepairStatusDto getRepairStatusById(Long id) {
        try {
            RepairStatus repairStatus = repairStatusRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("RepairStatus not found"));
            return convertToRepairStatusDto(repairStatus);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {

            throw new CustomException("Failed to retrieve repair status", e);
        }
    }


    // Update repair status
    public RepairStatus updateRepairStatus(Long id, RepairStatusDto repairStatusDto) {
        try {
            RepairStatus repairStatus = repairStatusRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("RepairStatus not found"));
            repairStatus.setStatusName(repairStatusDto.statusName);
            return repairStatusRepository.save(repairStatus);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update repair status", e);
        }
    }

    // Delete repair status by ID
    public void deleteRepairStatus(Long id) {
    try {
        RepairStatus existingRepairStatus = repairStatusRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Repair status not found with ID: " + id));;
        repairStatusRepository.delete(existingRepairStatus);
    } catch (Exception e) {
        throw new CustomException("failed to delete repair status,", e);
        }
    }
}
