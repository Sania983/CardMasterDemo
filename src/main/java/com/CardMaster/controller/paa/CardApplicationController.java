package com.CardMaster.controller.paa;

import com.CardMaster.dto.paa.CardApplicationDto;
import com.CardMaster.service.paa.CardApplicationService;
import com.CardMaster.dto.paa.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class CardApplicationController {

    private static final Logger log = LogManager.getLogger(CardApplicationController.class);
    private final CardApplicationService applicationService;

    // --- Submit Application ---
    @PostMapping
    public ResponseEntity<ResponseStructure<CardApplicationDto>> submit(@Valid @RequestBody CardApplicationDto dto) {
        log.info("Submitting card application");
        CardApplicationDto saved = applicationService.submit(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseStructure<>("Application submitted successfully", saved));
    }

    // --- Get Application by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<CardApplicationDto>> getById(@PathVariable Long id) {
        log.info("Fetching application {}", id);
        CardApplicationDto dto = applicationService.findById(id);
        return ResponseEntity.ok(new ResponseStructure<>("Application retrieved successfully", dto));
    }

    // --- Get All Applications ---
    @GetMapping
    public ResponseEntity<ResponseStructure<List<CardApplicationDto>>> getAllApplications() {
        log.info("Fetching all applications");
        List<CardApplicationDto> apps = applicationService.getAllApplications();
        return ResponseEntity.ok(new ResponseStructure<>("All applications retrieved successfully", apps));
    }

    // --- Delete Application ---
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<Void>> deleteApplication(@PathVariable Long id) {
        log.info("Deleting application {}", id);
        applicationService.deleteApplication(id);
        return ResponseEntity.ok(new ResponseStructure<>("Application deleted successfully", null));
    }
}
