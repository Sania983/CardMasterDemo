package com.CardMaster.controller.paa;

import com.CardMaster.dto.paa.CustomerDto;
import com.CardMaster.service.paa.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class
CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public CustomerDto createCustomer(@RequestBody CustomerDto dto) {
        return service.createCustomer(dto);
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable Long id) {
        return service.getCustomer(id);
    }

    @PutMapping("/{id}")
    public CustomerDto updateCustomer(@PathVariable Long id, @RequestBody CustomerDto dto) {
        return service.updateCustomer(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        service.deleteCustomer(id);
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return service.getAllCustomers();
    }
}
