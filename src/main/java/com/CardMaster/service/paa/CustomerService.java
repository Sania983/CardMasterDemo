package com.CardMaster.service.paa;
import com.CardMaster.dao.paa.CustomerRepository;
import com.CardMaster.dto.paa.CustomerDto;
import com.CardMaster.exception.paa.CustomerNotFoundException;
import com.CardMaster.mapper.paa.EntityMapper;
import com.CardMaster.model.paa.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {

        this.repo = repo;
    }

    public CustomerDto createCustomer(CustomerDto dto) {
        Customer entity = EntityMapper.toCustomerEntity(dto);
        return EntityMapper.toCustomerDto(repo.save(entity));
    }

    public CustomerDto getCustomer(Long id) {

        return repo.findById(id).map(EntityMapper::toCustomerDto).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public CustomerDto updateCustomer(Long id, CustomerDto dto) {
        Customer existing = repo.findById(id).orElseThrow();
        existing.setName(dto.getName());
        existing.setContactInfo(dto.getContactInfo());
        existing.setIncome(dto.getIncome());
        existing.setEmploymentType(Customer.EmploymentType.valueOf(dto.getEmploymentType()));
        existing.setStatus(Customer.CustomerStatus.valueOf(dto.getStatus()));
        return EntityMapper.toCustomerDto(repo.save(existing));
    }

    public void deleteCustomer(Long id) {
        repo.deleteById(id);
    }

    public List<CustomerDto> getAllCustomers() {
        return repo.findAll().stream().map(EntityMapper::toCustomerDto).toList();
    }
}
