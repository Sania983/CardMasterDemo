package com.CardMaster.controller.paa;

import com.CardMaster.dto.paa.CardApplicationDto;
import com.CardMaster.service.paa.CardApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class CardApplicationController {
    private final CardApplicationService service;

    public CardApplicationController(CardApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public CardApplicationDto submitApplication(@RequestBody CardApplicationDto dto) {
        return service.submitApplication(dto);
    }

    @GetMapping("/{id}")
    public CardApplicationDto getApplication(@PathVariable Long id) {
        return service.getApplication(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<CardApplicationDto> getApplicationsByCustomer(@PathVariable Long customerId) {
        return service.getApplicationsByCustomer(customerId);
    }

    @PutMapping("/{id}/status")
    public CardApplicationDto updateStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateApplicationStatus(id, status);
    }
}
