package nl.backend.reparatieservice.controller;

import nl.backend.reparatieservice.dto.RepairOptionDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.RepairOption;
import nl.backend.reparatieservice.service.RepairOptionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair-options")
public class RepairOptionController {

    private final RepairOptionService repairOptionService;


    public RepairOptionController(RepairOptionService repairOptionService) {
        this.repairOptionService = repairOptionService;
    }

    // Create a new repair option
    @PostMapping
    public ResponseEntity<?> createRepairOption(@RequestBody RepairOptionDto repairOptionDto) {
        try {
            RepairOption createdRepairOption = repairOptionService.createRepairOption(repairOptionDto);
            return new ResponseEntity<>(createdRepairOption, HttpStatus.CREATED);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create repair option: " + e.getMessage());
        }
    }

    // Retrieve all repair options
    @GetMapping
    public ResponseEntity<?> getAllRepairOptions() {
        try {
            List<RepairOptionDto> repairOptionDtos = repairOptionService.getAllRepairOptions();
            return new ResponseEntity<>(repairOptionDtos, HttpStatus.OK);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve repair options: " + e.getMessage());
        }
    }

    // Retrieve a repair option by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRepairOptionById(@PathVariable Long id) {
        try {
            RepairOptionDto repairOptionDto = repairOptionService.getRepairOptionById(id);
            if (repairOptionDto != null) {
                return new ResponseEntity<>(repairOptionDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve repair option: " + e.getMessage());
        }
    }

    // Update a repair option by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRepairOption(@PathVariable Long id, @RequestBody RepairOptionDto repairOptionDto) {
        try {
            RepairOption updatedRepairOption = repairOptionService.updateRepairOption(id, repairOptionDto);
            return new ResponseEntity<>(updatedRepairOption, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update repair option: " + e.getMessage());
        }
    }

    // Delete a repair option by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRepairOption(@PathVariable Long id) {
        try {
            repairOptionService.deleteRepairOption(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete repair option: " + e.getMessage());
        }
    }
}