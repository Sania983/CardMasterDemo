package com.CardMaster.controller.paa;

import com.CardMaster.dto.paa.CardApplicationDto;
import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.dao.paa.CardApplicationRepository;
import com.CardMaster.service.paa.CardApplicationService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/applications")
public class CardApplicationController {
    private final CardApplicationService service;
    private final CardApplicationRepository applicationRepo;

    public CardApplicationController(CardApplicationService service,
                                     CardApplicationRepository applicationRepo) {
        this.service = service;
        this.applicationRepo = applicationRepo;
    }

    // New endpoint: create application with just a requested limit
    @PostMapping("/create-app/{limit}")
    public Long createApplication(@PathVariable double limit) {
        CardApplication app = new CardApplication();
        app.setRequestedLimit(limit);
        app.setApplicationDate(LocalDate.now());
        app.setStatus(CardApplication.CardApplicationStatus.Submitted);
        app = applicationRepo.save(app);
        return app.getApplicationId();
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
