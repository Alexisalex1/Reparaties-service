package nl.backend.reparatieservice.controller;
import nl.backend.reparatieservice.dto.RepairStatusDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.RepairStatus;
import nl.backend.reparatieservice.service.RepairStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair-status")
public class RepairStatusController {

    private final RepairStatusService repairStatusService;

    @Autowired
    public RepairStatusController(RepairStatusService repairStatusService) {
        this.repairStatusService = repairStatusService;
    }

    // Create a new repair status
    @PostMapping
    public ResponseEntity<?> createRepairStatus(@RequestBody RepairStatusDto repairStatusDto) {
        try {
            RepairStatus createdRepairStatus = repairStatusService.createRepairStatus(repairStatusDto);
            return new ResponseEntity<>(createdRepairStatus, HttpStatus.CREATED);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create repair status: " + e.getMessage());
        }
    }

    // Retrieve all repair statuses
    @GetMapping
    public ResponseEntity<?> getAllRepairStatuses() {
        try {
            List<RepairStatusDto> repairStatuses = repairStatusService.getAllRepairStatuses();
            return new ResponseEntity<>(repairStatuses, HttpStatus.OK);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve repair statuses: " + e.getMessage());
        }
    }

    // Retrieve a repair status by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRepairStatusById(@PathVariable Long id) {
        try {
            RepairStatusDto repairStatusDto = repairStatusService.getRepairStatusById(id);
            return new ResponseEntity<>(repairStatusDto, HttpStatus.OK);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve repair status: " + e.getMessage());
        }
    }

    // Update a repair status by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRepairStatus(@PathVariable Long id, @RequestBody RepairStatusDto repairStatusDto) {
        try {
            RepairStatus updatedRepairStatus = repairStatusService.updateRepairStatus(id, repairStatusDto);
            return new ResponseEntity<>(updatedRepairStatus, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update repair status: " + e.getMessage());
        }
    }

    // Delete a repair status by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRepairStatus(@PathVariable Long id) {
        try {
            repairStatusService.deleteRepairStatus(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete repair status: " + e.getMessage());
        }
    }
}
