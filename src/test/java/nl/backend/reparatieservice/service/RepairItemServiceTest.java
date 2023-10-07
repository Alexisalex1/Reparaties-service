package nl.backend.reparatieservice.service;

import nl.backend.reparatieservice.dto.RepairItemDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.RepairItem;
import nl.backend.reparatieservice.repository.RepairItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RepairItemServiceTest {
    @Mock
    RepairItemRepository repairItemRepository;

    @InjectMocks
    RepairItemService repairItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void shouldcCorreclyCreateRepairItem() {
        RepairItemDto repairItemDto = new RepairItemDto();
        repairItemDto.itemName = "Item 1";
        repairItemDto.description = "Nog een item";
        repairItemDto.cost = new BigDecimal("50.00");

        Mockito.when(repairItemRepository.save(any(RepairItem.class)))
                .thenAnswer(invocation -> {
                    RepairItem repairItem = invocation.getArgument(0);
                    repairItem.setId(1L);
                    return repairItem;
                });
        RepairItem createdRepairItem = repairItemService.createRepairItem(repairItemDto);

        assertNotNull(createdRepairItem);
        assertEquals(1L, createdRepairItem.getId());
        assertEquals("Item 1", createdRepairItem.getItemName());
        assertEquals("Nog een item", createdRepairItem.getDescription());
        assertEquals(new BigDecimal("50.00"), createdRepairItem.getCost());

        Mockito.verify(repairItemRepository, times(1)).save(any(RepairItem.class));
    }


    @Test
    void shouldFailToCreate() {

        RepairItemDto repairItemDto = new RepairItemDto();
        repairItemDto.itemName = "Item 1?";
        repairItemDto.description = "Vraagteken bij 1?";
        repairItemDto.cost = new BigDecimal("50.00");

        Mockito. when(repairItemRepository.save(any(RepairItem.class))).thenThrow(new RuntimeException("Geen database meer"));
        assertThrows(CustomException.class, () -> repairItemService.createRepairItem(repairItemDto));
    }
    @Test
    void shouldGetAllItems() {

        RepairItem repairItem1 = new RepairItem();
        repairItem1.setId(1L);
        repairItem1.setItemName("Item met band");
        repairItem1.setDescription("beschrijving");
        repairItem1.setCost(new BigDecimal("50.00"));

        RepairItem repairItem2 = new RepairItem();
        repairItem2.setId(2L);
        repairItem2.setItemName("Item met twee banden");
        repairItem2.setDescription("Beschrijving");
        repairItem2.setCost(new BigDecimal("60.00"));

        Mockito.when(repairItemRepository.findAll()).thenReturn(List.of(repairItem1, repairItem2));


        List<RepairItemDto> repairItems = repairItemService.getAllRepairItems();

        assertNotNull(repairItems);
        assertEquals(2, repairItems.size());

        RepairItemDto item1 = repairItems.get(0);
        assertEquals(1L, item1.id);
        assertEquals("Item met band", item1.itemName);
        assertEquals("beschrijving", item1.description);
        assertEquals(new BigDecimal("50.00"), item1.cost);

        RepairItemDto item2 = repairItems.get(1);
        assertEquals(2L, item2.id);
        assertEquals("Item met twee banden", item2.itemName);
        assertEquals("Beschrijving", item2.description);
        assertEquals(new BigDecimal("60.00"), item2.cost);

        Mockito.verify(repairItemRepository, times(1)).findAll();
    }

    @Test
    void shouldExceptionWhenFailsToGet() {
        Mockito.when(repairItemRepository.findAll()).thenThrow(new RuntimeException("Geen database meer gevonden"));
        assertThrows(CustomException.class, () -> repairItemService.getAllRepairItems());
    }

    @Test
    void shouldGetRepairItemById() {
        Long id = 1L;
        RepairItem repairItem = new RepairItem();
        repairItem.setId(id);
        repairItem.setItemName("Dit is toch maar test");
        repairItem.setDescription("Ik schrijf hier dan ook test");
        repairItem.setCost(new BigDecimal("500.00"));

        Mockito.when(repairItemRepository.findById(id)).thenReturn(Optional.of(repairItem));
        RepairItemDto repairItemDto = repairItemService.getRepairItemById(id);

        assertNotNull(repairItemDto);
        assertEquals(id, repairItemDto.id);
        assertEquals("Dit is toch maar test", repairItemDto.itemName);
        assertEquals("Ik schrijf hier dan ook test", repairItemDto.description);
        assertEquals(new BigDecimal("500.00"), repairItemDto.cost);

        Mockito.verify(repairItemRepository, times(1)).findById(id);
    }

    @Test
    void ItemByIdNotFoundException() {
        Long id = 1L;

        Mockito.when(repairItemRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->{
            repairItemService.getRepairItemById(id);
        });
    }

    @Test
    void ShouldFailToGetId() {

        Long id = 1L;
        Mockito.when(repairItemRepository.findById(id)).thenThrow(new RuntimeException("Database connectie niet gevonden"));
        assertThrows(CustomException.class, () -> repairItemService.getRepairItemById(id));
    }

    @Test
    void shouldUpdateItem() {

        Long id = 1L;
        RepairItemDto repairItemDto = new RepairItemDto();
        repairItemDto.itemName = "Dit is geupdated";
        repairItemDto.description = "Andere woorden";
        repairItemDto.cost = new BigDecimal("230.00");

        RepairItem existingRepairItem = new RepairItem();
        existingRepairItem.setId(id);
        existingRepairItem.setItemName("Hier stond iets");
        existingRepairItem.setDescription("Iets ouds");
        existingRepairItem.setCost(new BigDecimal("130.00"));

        Mockito.when(repairItemRepository.findById(id)).thenReturn(Optional.of(existingRepairItem));
        Mockito.when(repairItemRepository.save(any(RepairItem.class))).thenReturn(existingRepairItem);


        RepairItem updatedRepairItem = repairItemService.updateRepairItem(id, repairItemDto);

        assertNotNull(updatedRepairItem);
        assertEquals(id, updatedRepairItem.getId());
        assertEquals("Dit is geupdated", updatedRepairItem.getItemName());
        assertEquals("Andere woorden", updatedRepairItem.getDescription());
        assertEquals(new BigDecimal("230.00"), updatedRepairItem.getCost());

        Mockito.verify(repairItemRepository, times(1)).findById(id);
        Mockito.verify(repairItemRepository, times(1)).save(existingRepairItem);
    }

    @Test
    void shouldExceoptionUpdateItemNotFound() {
        Long id = 1L;
        RepairItemDto repairItemDto = new RepairItemDto();
        repairItemDto.itemName = "Dit kost zoveel tijd";
        repairItemDto.description = "Echt niet normaal";
        repairItemDto.cost = new BigDecimal("200.00");

        Mockito.when(repairItemRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            repairItemService.updateRepairItem(id, repairItemDto);
        });

    }

    @Test
    void shouldCustomFailUpdate() {

        Long id = 1L;
        RepairItemDto repairItemDto = new RepairItemDto();
        repairItemDto.itemName = "Bijna klaar";
        repairItemDto.description = "Maar nog niet helemaal";
        repairItemDto.cost = new BigDecimal("30.00");

        RepairItem existingRepairItem = new RepairItem();
        existingRepairItem.setId(id);
        existingRepairItem.setItemName("Item 2 was hier");
        existingRepairItem.setDescription("Blijft 2");
        existingRepairItem.setCost(new BigDecimal("20.00"));

        Mockito.when(repairItemRepository.findById(id)).thenReturn(Optional.of(existingRepairItem));
        Mockito.when(repairItemRepository.save(any(RepairItem.class))).thenThrow(new RuntimeException("Niet gelukt met database"));

        assertThrows(CustomException.class, () -> repairItemService.updateRepairItem(id, repairItemDto));
    }

    @Test
    void shouldDeleteItem() {
        Long DeleteId = 1L;
        RepairItem existingRepairItem = new RepairItem();
        existingRepairItem.setId(DeleteId);

        Mockito.when(repairItemRepository.findById(DeleteId)).thenReturn(Optional.of(existingRepairItem));

        repairItemService.deleteRepairItem(DeleteId);

        Mockito.verify(repairItemRepository, times(1)).delete(existingRepairItem);
    }

    @Test
    void shouldNotDeleteItemNotFound() {

        Long IdDeleter = 1L;

        Mockito.when(repairItemRepository.findById(IdDeleter)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            repairItemService.deleteRepairItem(IdDeleter);
        });

    }

    @Test
    void shouldFailToDelete() {
        Long DeletethisId = 1L;
        RepairItem existingRepairItem = new RepairItem();
        existingRepairItem.setId(DeletethisId);

        Mockito.when(repairItemRepository.findById(DeletethisId)).thenReturn(Optional.of(existingRepairItem));
        doThrow(new RuntimeException("Database gefaald")).when(repairItemRepository).delete(existingRepairItem);

        assertThrows(CustomException.class, () -> repairItemService.deleteRepairItem(DeletethisId));
    }
}