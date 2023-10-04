package nl.backend.reparatieservice.service;

import jakarta.persistence.EntityNotFoundException;
import nl.backend.reparatieservice.dto.*;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.model.*;
import nl.backend.reparatieservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepairRequestService {

    private final CustomerRepository customerRepository;
    private final RepairRequestRepository repairRequestRepository;
    private final RepairStatusRepository repairStatusRepository;
    private final RepairItemRepository repairItemRepository;
    private final RepairOptionRepository repairOptionRepository;
    private final InvoiceRepository invoiceRepository;


    @Autowired
    public RepairRequestService(
            CustomerRepository customerRepository,
            RepairRequestRepository repairRequestRepository,
            RepairStatusRepository repairStatusRepository,
            RepairItemRepository  repairItemRepository,
            RepairOptionRepository repairOptionRepository,
            InvoiceRepository invoiceRepository


    ) {
        this.customerRepository = customerRepository;
        this.repairRequestRepository = repairRequestRepository;
        this.repairStatusRepository = repairStatusRepository;
        this.repairItemRepository = repairItemRepository;
        this.repairOptionRepository = repairOptionRepository;
        this.invoiceRepository = invoiceRepository;

    }

    //converters for DTO usage

    private UploadedPhotoDto convertToUploadedPhotoDto(UploadedPhoto uploadedPhoto) {
        UploadedPhotoDto uploadedPhotoDto = new UploadedPhotoDto();
        uploadedPhotoDto.id = uploadedPhoto.getId();
        uploadedPhotoDto.fileName = uploadedPhoto.getFileName();
        uploadedPhotoDto.uploadedPhotoUrl = uploadedPhoto.getUploadedPhotoUrl();
        return uploadedPhotoDto;
    }
    private CustomerDto convertToCustomerDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.customerId = customer.getCustomerId();
        customerDto.name =  customer.getName();
        customerDto.email = customer.getEmail();
        customerDto.phoneNumber = customer.getPhoneNumber();
        return customerDto;
    }

    private RepairItemDto convertToRepairItemDto(RepairItem repairItem) {
        RepairItemDto repairItemDto = new RepairItemDto();
        repairItemDto.id = repairItem.getId();
        repairItemDto.itemName = repairItem.getItemName();
        repairItemDto.description = repairItem.getDescription();
        repairItemDto.cost = repairItem.getCost();
        return repairItemDto;
    }

    private RepairStatusDto convertToRepairStatusDto(RepairStatus repairStatus) {
        RepairStatusDto repairStatusDto = new RepairStatusDto();
        repairStatusDto.Id =repairStatus.getId();
        repairStatusDto.statusName = repairStatus.getStatusName();

        return repairStatusDto;
    }

    private RepairRequestDto convertToRepairRequestDto(RepairRequest repairRequest) {
       RepairRequestDto repairRequestDto = new RepairRequestDto();
        repairRequestDto.requestId = repairRequest.getRequestId();
        repairRequestDto.repairDate = repairRequest.getRepairDate();
        repairRequestDto.repairDescription = repairRequest.getRepairDescription();
        repairRequestDto.totalCost = repairRequest.getTotalCost();
        repairRequestDto.invoice = repairRequest.getInvoice();repairRequestDto.invoice = repairRequest.getInvoice();
        repairRequestDto.repairStatus = convertToRepairStatusDto(repairRequest.getRepairStatus());

        repairRequestDto.customer = convertToCustomerDto(repairRequest.getCustomer());

        List<UploadedPhotoDto> uploadedPhotoDtos = repairRequest.getUploadedPhotos().stream()
                .map(this::convertToUploadedPhotoDto)
                .collect(Collectors.toList());
        repairRequestDto.setUploadedPhotos(uploadedPhotoDtos);

        List<RepairOptionDto> repairOptionDtos = repairRequest
        .getRepairOptions().stream()
                .map(this::convertToRepairOptionDto)
                .collect(Collectors.toList());
        repairRequestDto.setRepairOptions(repairOptionDtos);

        List<RepairItemDto> repairItemDtos = repairRequest.getRepairItems().stream()
                .map(this::convertToRepairItemDto)
                .collect(Collectors.toList());
        repairRequestDto.setRepairItems(repairItemDtos);

        return repairRequestDto;
    }
    private List<RepairRequestDto> convertToRepairRequestDtoList(List<RepairRequest> repairRequests) {
        return repairRequests.stream()
                .map(this::convertToRepairRequestDto)
                .collect(Collectors.toList());
    }

    private RepairOptionDto convertToRepairOptionDto(RepairOption repairOption) {
        RepairOptionDto repairOptionDto = new RepairOptionDto();
        repairOptionDto.Id = repairOption.getId();
        repairOptionDto.optionName = repairOption.getOptionName();
        repairOptionDto.description = repairOption.getDescription();
        repairOptionDto.cost = repairOption.getCost();

        return repairOptionDto;
    }

    // Calculate the total cost of a repair request and adds it to the invoicw
    private BigDecimal calculateTotalCost(RepairRequest repairRequest) {
        BigDecimal totalCost = BigDecimal.ZERO;
        if (repairRequest.getRepairItems() != null) {
            for (RepairItem repairItem : repairRequest.getRepairItems()) {
                totalCost = totalCost.add(repairItem.getCost());
            }
        }

        if (repairRequest.getRepairOptions() != null) {
            for (RepairOption repairOption : repairRequest.getRepairOptions()) {
                totalCost = totalCost.add(repairOption.getCost());
            }
        }
        return totalCost;
    }

    private void updateTotalCostAndInvoice(RepairRequest repairRequest) {

        BigDecimal totalCostForRepairRequest = calculateTotalCost(repairRequest);
        BigDecimal taxRate = new BigDecimal("0.21"); // 21% tax rate
        BigDecimal taxForRepairRequest = totalCostForRepairRequest.multiply(taxRate);
        BigDecimal shippingCost = new BigDecimal("4.00");

        BigDecimal taxForRepairItems = totalCostForRepairRequest.subtract(shippingCost).multiply(taxRate);

        repairRequest.setTotalCost(totalCostForRepairRequest.add(shippingCost));

        BigDecimal totalCostForInvoice = totalCostForRepairRequest;
        BigDecimal taxForInvoice = taxForRepairItems;
        BigDecimal totalAmountForInvoice = totalCostForInvoice.add(taxForInvoice).add(shippingCost);

        Invoice invoice = repairRequest.getInvoice();
        invoice.setRepairItemsCost(totalCostForInvoice);
        invoice.setRepairOptionsCost(BigDecimal.ZERO);
        invoice.setTax(taxForInvoice);
        invoice.setTotalAmount(totalAmountForInvoice);
        invoice.setShippingCost(shippingCost);


        repairRequestRepository.save(repairRequest);
        invoiceRepository.save(invoice);
    }

    //Creates repairRequest
    @Transactional
    public RepairRequest createRepairRequest(
            Long customerId,
            RepairRequestDto requestDto
    ) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        RepairRequest repairRequest = new RepairRequest();
        repairRequest.setRepairDescription(requestDto.repairDescription);
        repairRequest.setRepairDate(LocalDateTime.now());
        repairRequest.setCustomer(customer);

        Optional<RepairStatus> pendingStatusOptional = repairStatusRepository.findByStatusName("Pending");

        if (pendingStatusOptional.isPresent()) {

            repairRequest.setRepairStatus(pendingStatusOptional.get());
        } else {

            RepairStatus pendingStatus = new RepairStatus("Pending");
            repairRequest.setRepairStatus(pendingStatus);
        }

        BigDecimal totalCost = calculateTotalCost(repairRequest);
        BigDecimal taxRate = new BigDecimal("0.21"); // 21% tax rate
        BigDecimal tax = totalCost.multiply(taxRate);
        BigDecimal shippingCost = new BigDecimal("4.00");
        totalCost = totalCost.add(shippingCost);

        repairRequest.setTotalCost(totalCost);

        try {
            return repairRequestRepository.save(repairRequest);
        } catch (Exception e) {
            throw new CustomException("Failed to create repair request", e);
        }
    }

    //retrieve request by id
    public RepairRequestDto getRepairRequestById(Long repairRequestId) {
        RepairRequest repairRequest = repairRequestRepository.findById(repairRequestId).orElseThrow(() -> new nl.backend.reparatieservice.exception.EntityNotFoundException("Repair request not found.."));
        return convertToRepairRequestDto(repairRequest);
    }

    @Transactional
    // Retrieve all repair requests
    public List<RepairRequestDto> getAllRepairRequests() {
        try {
            List<RepairRequest> repairRequests = (List<RepairRequest>) repairRequestRepository.findAll();
            return repairRequests.stream()
                    .map(this::convertToRepairRequestDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all repair requests.", e);
        }
    }

    //Retrieve repair request with corresponding customer
    public List<RepairRequestDto> getRepairRequestsByCustomerId(Long customerId) {
        try {
            List<RepairRequest> repairRequests = repairRequestRepository.findByCustomerCustomerId(customerId);
            return convertToRepairRequestDtoList(repairRequests);
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve repair requests by customer ID.", e);
        }
    }

    @Transactional
    // Add the repair item to the repair request
    public RepairRequestDto addRepairItemToRequest(Long requestId, Long itemId) {
        try {
            RepairRequest repairRequest = repairRequestRepository.findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException("RepairRequest not found"));

            RepairItem repairItem = repairItemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("RepairItem not found"));

            repairRequest.getRepairItems().add(repairItem);
            repairRequest = repairRequestRepository.save(repairRequest);
            updateTotalCostAndInvoice(repairRequest);

            return convertToRepairRequestDto(repairRequest);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to add repair item to the request.", e);
        }
    }

    @Transactional
    // Remove the repair item from the repair request
    public RepairRequestDto removeRepairItemFromRequest(Long requestId, Long itemId) {
        try {
            RepairRequest repairRequest = repairRequestRepository.findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException("RepairRequest not found"));

            RepairItem repairItem = repairItemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("RepairItem not found"));

            repairRequest.getRepairItems().remove(repairItem);
            repairRequest = repairRequestRepository.save(repairRequest);
            updateTotalCostAndInvoice(repairRequest);

            return convertToRepairRequestDto(repairRequest);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to remove repair item from the request.", e);
        }
    }

    @Transactional
    // Add the repair option to the repair request
    public RepairRequestDto addRepairOptionToRequest(Long requestId, Long optionId) {
        try {
            RepairRequest repairRequest = repairRequestRepository.findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException("RepairRequest not found"));

            RepairOption repairOption = repairOptionRepository.findById(optionId)
                    .orElseThrow(() -> new EntityNotFoundException("RepairOption not found"));

            repairRequest.getRepairOptions().add(repairOption);
            repairRequest = repairRequestRepository.save(repairRequest);
            updateTotalCostAndInvoice(repairRequest);

            return convertToRepairRequestDto(repairRequest);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to add repair option to the request.", e);
        }
    }

    @Transactional
    // Remove the repair option from the repair request
    public RepairRequestDto removeRepairOptionFromRequest(Long requestId, Long optionId) {
        try {
            RepairRequest repairRequest = repairRequestRepository.findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException("RepairRequest not found"));

            RepairOption repairOption = repairOptionRepository.findById(optionId)
                    .orElseThrow(() -> new EntityNotFoundException("RepairOption not found"));

            repairRequest.getRepairOptions().remove(repairOption);
            repairRequest = repairRequestRepository.save(repairRequest);
            updateTotalCostAndInvoice(repairRequest);

            return convertToRepairRequestDto(repairRequest);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to remove repair option from the request.", e);
        }
    }


    // Update the status by statusId.
    public RepairRequestDto updateRepairRequestStatus(Long requestId, Long newId) {
        try {
            RepairRequest repairRequest = repairRequestRepository.findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException("RepairRequest not found"));

            Optional<RepairStatus> newStatusOptional = repairStatusRepository.findById(newId);

            if (newStatusOptional.isPresent()) {
                repairRequest.setRepairStatus(newStatusOptional.get());
                repairRequest = repairRequestRepository.save(repairRequest);

                return convertToRepairRequestDto(repairRequest);
            } else {
                throw new EntityNotFoundException("New status ID not found");
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update repair request status.", e);
        }
    }
}

