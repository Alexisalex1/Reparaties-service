package nl.backend.reparatieservice.service;

import nl.backend.reparatieservice.dto.RepairOptionDto;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.model.RepairOption;
import nl.backend.reparatieservice.repository.RepairOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairOptionService {

    private final RepairOptionRepository repairOptionRepository;


    public RepairOptionService(
            RepairOptionRepository repairOptionRepository) {
        this.repairOptionRepository = repairOptionRepository;
    }

    private RepairOptionDto converToRepairOptionDto(RepairOption repairOption) {
        RepairOptionDto repairOptionDto = new RepairOptionDto();
        repairOptionDto.id = repairOption.getId();
        repairOptionDto.optionName = repairOption.getOptionName();
        repairOptionDto.description = repairOption.getDescription();
        repairOptionDto.cost = repairOption.getCost();

        return repairOptionDto;
    }

    // create a new repair option(if needed)
    public RepairOption createRepairOption(RepairOptionDto repairOptionDto) {
        try {
            RepairOption repairOption = new RepairOption();
            repairOption.setOptionName(repairOptionDto.optionName);
            repairOption.setDescription(repairOptionDto.description);
            repairOption.setCost(repairOptionDto.cost);

            return repairOptionRepository.save(repairOption);
    } catch (Exception e) {
        throw new CustomException("Failed to create repair option", e);
    }
    }

    // retrieve all repair options
    public List<RepairOptionDto> getAllRepairOptions() {
        try {
            List<RepairOption>  repairOptions = (List<RepairOption>) repairOptionRepository.findAll();
            return repairOptions.stream()
                .map(this::converToRepairOptionDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve repair options", e);
        }
    }

    // find repair option by id
    public RepairOptionDto getRepairOptionById(Long id) {
        try {
            RepairOption repairOption = repairOptionRepository
                    .findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("RepairOption not found"));

            return converToRepairOptionDto(repairOption);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve repair option", e);
        }
    }

    //Update repair option
    public RepairOption updateRepairOption(Long id, RepairOptionDto repairOptionDto) {
        try {
        RepairOption repairOption = repairOptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RepairOption not found"));

        repairOption.setOptionName(repairOptionDto.optionName);
        repairOption.setDescription(repairOptionDto.description);
        repairOption.setCost(repairOptionDto.cost);

        return repairOptionRepository.save(repairOption);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update repair option", e);
        }
    }

    // delete option
    public void deleteRepairOption(Long id) {
        try {
            RepairOption existingRepairOption = repairOptionRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("RepairItem not found with ID: " + id));
            repairOptionRepository.delete(existingRepairOption);
        } catch (EntityNotFoundException e) {
                throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete repair option", e);
        }
    }
}
