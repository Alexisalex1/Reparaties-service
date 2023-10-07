package nl.backend.reparatieservice.controller;

import nl.backend.reparatieservice.dto.RepairItemDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.RepairItem;
import nl.backend.reparatieservice.service.RepairItemService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair-items")
public class RepairItemController {

    private final RepairItemService repairItemService;

    public RepairItemController(RepairItemService repairItemService) {
        this.repairItemService = repairItemService;
    }

    // Create a new repair item
    @PostMapping
    public ResponseEntity<?> createRepairItem(@RequestBody RepairItemDto repairItemDto) {
        try {
            RepairItem createdRepairItem = repairItemService.createRepairItem(repairItemDto);
            return new ResponseEntity<>(createdRepairItem, HttpStatus.CREATED);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create repair item: " + e.getMessage());
        }
    }

    // Retrieve all repair items
    @GetMapping
    public ResponseEntity<?> getAllRepairItems() {
        try {
            List<RepairItemDto> repairItems = repairItemService.getAllRepairItems();
            return new ResponseEntity<>(repairItems, HttpStatus.OK);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve repair items: " + e.getMessage());
        }
    }

    // Retrieve a repair item by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRepairItemById(@PathVariable Long id) {
        try {
            RepairItemDto repairItemDto = repairItemService.getRepairItemById(id);
            if (repairItemDto != null) {
                return new ResponseEntity<>(repairItemDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve repair item: " + e.getMessage());
        }
    }

    // Update a repair item by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRepairItem(@PathVariable Long id, @RequestBody RepairItemDto repairItemDto) {
        try {
            RepairItem updatedRepairItem = repairItemService.updateRepairItem(id, repairItemDto);
            return new ResponseEntity<>(updatedRepairItem, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update repair item: " + e.getMessage());
        }
    }

    // Delete a repair item by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRepairItem(@PathVariable Long id) {
        try {
            repairItemService.deleteRepairItem(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete repair item: " + e.getMessage());
        }
    }
}
