package nl.backend.reparatieservice.controller;

import nl.backend.reparatieservice.dto.RepairRequestDto;

import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.RepairRequest;
import nl.backend.reparatieservice.service.RepairRequestService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RepairRequestController {

    private final RepairRequestService repairRequestService;


    public RepairRequestController(RepairRequestService repairRequestService) {
        this.repairRequestService = repairRequestService;
    }

    // Create a new repair request attaching to a customer
    @PostMapping
    public ResponseEntity<?> createRepairRequest(
            @RequestParam Long customerId,
            @RequestBody RepairRequestDto requestDto) {
        try {
            RepairRequest createdRepairRequest = repairRequestService.createRepairRequest(customerId, requestDto);
            return new ResponseEntity<>(createdRepairRequest, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create repair request: " + e.getMessage());
        }
    }

    // Retrieve a repair request by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRepairRequestById(@PathVariable Long id) {
        try {
            RepairRequestDto repairRequest = repairRequestService.getRepairRequestById(id);
            return new ResponseEntity<>(repairRequest, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Repair request not found");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve repair request: " + e.getMessage());
        }
    }

    // Retrieves all repair requests
    @GetMapping
    public ResponseEntity<?> getAllRepairRequests() {
        try {
            List<RepairRequestDto> repairRequests = repairRequestService.getAllRepairRequests();
            return new ResponseEntity<>(repairRequests, HttpStatus.OK);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve repair requests: " + e.getMessage());
        }
    }


    // Retrieve all repair requests attached with a specific customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getRepairRequestsByCustomerId(@PathVariable Long customerId) {
        try {
            List<RepairRequestDto> repairRequests = repairRequestService.getRepairRequestsByCustomerId(customerId);
            return new ResponseEntity<>(repairRequests, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve repair requests: " + e.getMessage());
        }
    }

    // Adding a repair item to a repair request
    @PostMapping("/{requestId}/add-repair-item/{itemId}")
    public ResponseEntity<?> addRepairItemToRequest(
            @PathVariable Long requestId,
            @PathVariable Long itemId
    ) {
        try {
            RepairRequestDto updatedRepairRequest = repairRequestService.addRepairItemToRequest(requestId, itemId);
            return new ResponseEntity<>(updatedRepairRequest, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add repair item: " + e.getMessage());
        }
    }
    // Remove a repair item from a repair request
    @DeleteMapping("/{requestId}/remove-repair-item/{itemId}")
    public ResponseEntity<?> removeRepairItemFromRequest(
            @PathVariable Long requestId,
            @PathVariable Long itemId
    ) {
        try {
            RepairRequestDto updatedRepairRequest = repairRequestService.removeRepairItemFromRequest(requestId, itemId);
            return new ResponseEntity<>(updatedRepairRequest, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove repair item: " + e.getMessage());
        }
    }

    //add repair option
    @PostMapping("{requestId}/add-repair-option/{optionId}")
    public ResponseEntity<?> addRepairOptionToRequest(
            @PathVariable Long requestId,
            @PathVariable Long optionId
    ) {
        try {
            RepairRequestDto updatedRequest = repairRequestService.addRepairOptionToRequest(requestId, optionId);
            return new ResponseEntity<>(updatedRequest, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add repair option: " + e.getMessage());
        }
    }

    //remove option from request
    @DeleteMapping("/{requestId}/remove-repair-option/{optionId}")
    public ResponseEntity<?> removeRepairOptionFromRequest(
            @PathVariable Long requestId,
            @PathVariable Long optionId
    ) {
        try {
            RepairRequestDto updatedRepairRequest = repairRequestService.removeRepairOptionFromRequest(requestId, optionId);
            return new ResponseEntity<>(updatedRepairRequest, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove repair option: " + e.getMessage());
        }
    }

    // Update the status of a repair request by ID
    @PutMapping("/{requestId}/update-status")
    public ResponseEntity<?> updateRepairRequestStatus(
            @PathVariable Long requestId,
            @RequestParam Long newStatusId
    ) {
        try {
            RepairRequestDto updatedRepairRequest = repairRequestService.updateRepairRequestStatus(requestId, newStatusId);
            return new ResponseEntity<>(updatedRepairRequest, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update status: " + e.getMessage());
        }
    }

}
