package nl.backend.reparatieservice.service;

import nl.backend.reparatieservice.dto.RepairItemDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.RepairItem;
import nl.backend.reparatieservice.repository.RepairItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairItemService {

    private final RepairItemRepository repairItemRepository;

    @Autowired
    public RepairItemService(RepairItemRepository repairItemRepository) {
        this.repairItemRepository = repairItemRepository;
    }

    private RepairItemDto convertToRepairItemDto(RepairItem repairItem) {
        RepairItemDto repairItemDto = new RepairItemDto();
        repairItemDto.id = repairItem.getId();
        repairItemDto.itemName = repairItem.getItemName();
        repairItemDto.description = repairItem.getDescription();
        repairItemDto.cost = repairItem.getCost();
        return repairItemDto;

    }

    // create a new repair item (if needed)
    public RepairItem createRepairItem(RepairItemDto repairItemDto) {
        try {
            RepairItem repairItem = new RepairItem();
            repairItem.setItemName(repairItemDto.itemName);
            repairItem.setDescription(repairItemDto.description);
            repairItem.setCost(repairItemDto.cost);

            return repairItemRepository.save(repairItem);
        } catch (Exception e) {
            throw new CustomException("Failed to create repair item", e);
        }
    }

    // retrieves all repair items
    public List<RepairItemDto> getAllRepairItems() {
        try {
            List<RepairItem> repairItems = (List<RepairItem>) repairItemRepository.findAll();
            return repairItems.stream()
                    .map(this::convertToRepairItemDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve repair items", e);
        }
    }
    // retrieve repair item by id
    public RepairItemDto getRepairItemById(Long id) {
        try {
            RepairItem repairItem = repairItemRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("RepairItem not found with ID: " + id));
            return convertToRepairItemDto(repairItem);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve repair item", e);
        }
    }

    //update specific repair item
    public RepairItem updateRepairItem(Long id, RepairItemDto repairItemDto) {
        try {
        RepairItem existingRepairItem = repairItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RepairItem not found with ID: " + id));

        existingRepairItem.setItemName(repairItemDto.itemName);
        existingRepairItem.setDescription(repairItemDto.description);
        existingRepairItem.setCost(repairItemDto.cost);

        return repairItemRepository.save(existingRepairItem);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update repair item", e);
        }
    }

    // delete repair item if needed
    public void deleteRepairItem(Long id) {
        try {
            RepairItem existingRepairItem = repairItemRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("RepairItem not found with ID: " + id));
            repairItemRepository.delete(existingRepairItem);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete repair item", e);
        }
    }
}
