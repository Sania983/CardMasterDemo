package com.CardMaster.service.paa;

import com.CardMaster.dao.paa.CardApplicationRepository;
import com.CardMaster.dto.paa.CardApplicationDto;
import com.CardMaster.exception.paa.ApplicationNotFoundException;
import com.CardMaster.exception.paa.CustomerNotFoundException;
import com.CardMaster.mapper.paa.EntityMapper;
import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.model.paa.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardApplicationService {
    private final CardApplicationRepository repo;
    private final CustomerRepository customerRepo;


    public CardApplicationService(CardApplicationRepository repo, CustomerRepository customerRepo) {
        this.repo = repo;
        this.customerRepo = customerRepo;
    }

    public CardApplicationDto submitApplication(CardApplicationDto dto) {
        Customer customer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(dto.getCustomerId()));
        CardApplication app = EntityMapper.toCardApplicationEntity(dto, customer);
        app.setStatus(CardApplication.CardApplicationStatus.Submitted);
        return EntityMapper.toCardApplicationDto(repo.save(app));
    }

    public CardApplicationDto getApplication(Long id) {
        return repo.findById(id)
                .map(EntityMapper::toCardApplicationDto)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
    }

    public List<CardApplicationDto> getApplicationsByCustomer(Long customerId) {
        return repo.findByCustomerCustomerId(customerId).stream()
                .map(EntityMapper::toCardApplicationDto).toList();
    }


    public CardApplicationDto updateApplicationStatus(Long id, String status) {
        CardApplication app = repo.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
        try {
            app.setStatus(CardApplication.CardApplicationStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new ApplicationNotFoundException("Invalid status value: " + status);
        }
        return EntityMapper.toCardApplicationDto(repo.save(app));
    }
}
