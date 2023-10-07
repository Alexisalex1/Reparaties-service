package nl.backend.reparatieservice.service;
import nl.backend.reparatieservice.dto.RepairOptionDto;

import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.RepairOption;
import nl.backend.reparatieservice.repository.RepairOptionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class RepairOptionServiceTest {

    @Mock
    RepairOptionRepository repairOptionRepository;

    @InjectMocks
    RepairOptionService repairOptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCorectlyCreateRepairOption() {

        RepairOptionDto repairOptionDto = new RepairOptionDto();
        repairOptionDto.optionName = "Optie 1";
        repairOptionDto.description = "Optie 1 is leuk";
        repairOptionDto.cost = new BigDecimal("100.00");


        Mockito.when(repairOptionRepository.save(any(RepairOption.class)))
                .thenAnswer(invocation -> {
                    RepairOption repairOption = invocation.getArgument(0);
                    repairOption.setId(1L);
                    return repairOption;
                });
        RepairOption createdRepairOption = repairOptionService.createRepairOption(repairOptionDto);


        assertNotNull(createdRepairOption);
        assertEquals(1L, createdRepairOption.getId());
        assertEquals("Optie 1", createdRepairOption.getOptionName());
        assertEquals("Optie 1 is leuk", createdRepairOption.getDescription());
        assertEquals(new BigDecimal("100.00"), createdRepairOption.getCost());


        Mockito.verify(repairOptionRepository, times(1)).save(any(RepairOption.class));
    }

    @Test
    void shouldNotCreateRepairOption() {
        RepairOptionDto repairOptionDto = new RepairOptionDto();
        repairOptionDto.optionName = "Optie 1";
        repairOptionDto.description = "Optie 1 is leuk";
        repairOptionDto.cost = new BigDecimal("100.00");

        Mockito.when(repairOptionRepository.save(any(RepairOption.class)))
                .thenThrow(new RuntimeException("Niet gelukt"));

        assertThrows(CustomException.class, () -> {
            repairOptionService.createRepairOption(repairOptionDto);
        });
    }
    @Test
    void shouldGetCorrectRepairOption() {

        RepairOption repairOption = new RepairOption();
        repairOption.setId(1L);
        repairOption.setOptionName("Optie 1 net gemaakt");
        repairOption.setDescription("Optie 1 is net gemaakt verzinsel");
        repairOption.setCost(new BigDecimal("100.00"));


        Mockito.when(repairOptionRepository.findById(1L)).thenReturn(Optional.of(repairOption));


        RepairOptionDto foundRepairOption = repairOptionService.getRepairOptionById(1L);

        assertEquals(1L, foundRepairOption.id);
        assertEquals("Optie 1 net gemaakt", foundRepairOption.optionName);
        assertEquals("Optie 1 is net gemaakt verzinsel", foundRepairOption.description);
        assertEquals(new BigDecimal("100.00"), foundRepairOption.cost);
    }

    @Test
    void shouldGetNonExistingRepairOption() {
        Long nonExistingId = 123L;

        Mockito.when(repairOptionRepository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            repairOptionService.getRepairOptionById(nonExistingId);
        });
    }

    @Test
    void shouldCustomExceptionWhenNotGetById() {
        Long id = 1L;

        Mockito.when(repairOptionRepository.findById(id)).thenThrow(new RuntimeException("GELUKT"));

        assertThrows(CustomException.class, () -> repairOptionService.getRepairOptionById(id));
    }

    @Test
    void shouldSuccesfullyUpdateRepairOption() {

        Long id = 1L;
        RepairOptionDto repairOptionDto = new RepairOptionDto();
        repairOptionDto.optionName = "Ik ben cool";
        repairOptionDto.description = "Want ik ben Alexander";
        repairOptionDto.cost = new BigDecimal("200.00");

        RepairOption existingRepairOption = new RepairOption();
        existingRepairOption.setId(id);
        existingRepairOption.setOptionName("Ik wil niet meer");
        existingRepairOption.setDescription("Ik wil niet meer testen");
        existingRepairOption.setCost(new BigDecimal("100.00"));

        Mockito.when(repairOptionRepository.findById(id)).thenReturn(Optional.of(existingRepairOption));
        Mockito.when(repairOptionRepository.save(any(RepairOption.class))).thenReturn(existingRepairOption);

        RepairOption updatedRepairOption = repairOptionService.updateRepairOption(id, repairOptionDto);

        assertNotNull(updatedRepairOption);
        assertEquals(id, updatedRepairOption.getId());
        assertEquals("Ik ben cool", updatedRepairOption.getOptionName());
        assertEquals("Want ik ben Alexander", updatedRepairOption.getDescription());
        assertEquals(new BigDecimal("200.00"), updatedRepairOption.getCost());

        Mockito.verify(repairOptionRepository, times(1)).findById(id);
        Mockito.verify(repairOptionRepository, times(1)).save(existingRepairOption);
    }
    @Test
    void ShouldNotFindUpdateRepairOption() {

        Long id = 1L;
        RepairOptionDto repairOptionDto = new RepairOptionDto();
        repairOptionDto.optionName = "Updaten";
        repairOptionDto.description = "Lukt niet";
        repairOptionDto.cost = new BigDecimal("200.00");

        Mockito.when(repairOptionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> repairOptionService.updateRepairOption(id, repairOptionDto));

        Mockito.verify(repairOptionRepository, times(1)).findById(id);
    }

    @Test
    void whenUpdatingRepairOptionFails() {
        Long id = 1L;
        RepairOptionDto repairOptionDto = new RepairOptionDto();
        repairOptionDto.optionName = "Dit lukt niet";
        repairOptionDto.description = "Helaas lukt niet";
        repairOptionDto.cost = new BigDecimal("200.00");

        Mockito.when(repairOptionRepository.findById(id)).thenReturn(Optional.of(new RepairOption()));
        Mockito.when(repairOptionRepository.save(any(RepairOption.class)))
                .thenThrow(new RuntimeException("Het is gelukt dat het niet gelukt is"));

        assertThrows(CustomException.class, () -> {
            repairOptionService.updateRepairOption(id, repairOptionDto);
        });
    }

    @Test
    void itIsDeletedRepairOption() {
        Long deleteThisPlease = 1L;
        RepairOption existingRepairOption = new RepairOption();
        existingRepairOption.setId(deleteThisPlease);

        Mockito.when(repairOptionRepository.findById(deleteThisPlease))
                .thenReturn(Optional.of(existingRepairOption));

        repairOptionService.deleteRepairOption(deleteThisPlease);

        Mockito.verify(repairOptionRepository, times(1)).delete(existingRepairOption);
    }

    @Test
    void ShouldNotFindDeleteRepairOption() {
        Long notGointToDelete = 1L;

        Mockito.when(repairOptionRepository.findById(notGointToDelete))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            repairOptionService.deleteRepairOption(notGointToDelete);
        });
    }

    @Test
    void shouldFailCustomExceptionDelete() {
        Long idToDelete = 1L;

        Mockito.when(repairOptionRepository.findById(idToDelete)).thenThrow(new RuntimeException("Database is ontploft"));

        assertThrows(CustomException.class, () -> repairOptionService.deleteRepairOption(idToDelete));
    }

    @Test
    void shouldGetAllTheRepairOptions() {

        RepairOption repairOption1 = new RepairOption();
        repairOption1.setId(1L);
        repairOption1.setOptionName("Kies mij");
        repairOption1.setDescription("Iets staat hier");
        repairOption1.setCost(BigDecimal.valueOf(100.00));

        RepairOption repairOption2 = new RepairOption();
        repairOption2.setId(2L);
        repairOption2.setOptionName("Bijna klaar?");
        repairOption2.setDescription("Beschrijving daarvan");
        repairOption2.setCost(BigDecimal.valueOf(200.00));

        List<RepairOption> repairOptionList = Arrays.asList(repairOption1, repairOption2);

        Mockito.when(repairOptionRepository.findAll()).thenReturn(repairOptionList);

        List<RepairOptionDto> repairOptionDtoList = repairOptionService.getAllRepairOptions();

        assertNotNull(repairOptionDtoList);
        assertEquals(2, repairOptionDtoList.size());

        RepairOptionDto repairOptionDto1 = repairOptionDtoList.get(0);
        assertEquals(1L, repairOptionDto1.id);
        assertEquals("Kies mij", repairOptionDto1.optionName);
        assertEquals("Iets staat hier", repairOptionDto1.description);
        assertEquals(BigDecimal.valueOf(100.00), repairOptionDto1.cost);

        RepairOptionDto repairOptionDto2 = repairOptionDtoList.get(1);
        assertEquals(2L, repairOptionDto2.id);
        assertEquals("Bijna klaar?", repairOptionDto2.optionName);
        assertEquals("Beschrijving daarvan", repairOptionDto2.description);
        assertEquals(BigDecimal.valueOf(200.00), repairOptionDto2.cost);
    }
    @Test
    void shouldFailRepository() {

        Mockito.when(repairOptionRepository.findAll()).thenThrow(new RuntimeException("Deze was ik vergeten"));

        assertThrows(CustomException.class, () -> repairOptionService.getAllRepairOptions());
    }
}