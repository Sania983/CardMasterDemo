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

    // --- Create Application ---
    @PostMapping
    public ResponseEntity<ResponseStructure<CardApplicationDto>> create(
            @Valid @RequestBody CardApplicationDto dto,
            @RequestHeader("Authorization") String token) {

        log.info("Creating card application");
        CardApplicationDto saved = applicationService.create(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseStructure<>("Application created successfully", saved));
    }

    // --- Get Application by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<CardApplicationDto>> getById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        log.info("Fetching application {}", id);
        CardApplicationDto dto = applicationService.findById(id, token);
        return ResponseEntity.ok(new ResponseStructure<>("Application retrieved successfully", dto));
    }

    // --- Get All Applications ---
    @GetMapping
    public ResponseEntity<ResponseStructure<List<CardApplicationDto>>> getAllApplications(
            @RequestHeader("Authorization") String token) {

        log.info("Fetching all applications");
        List<CardApplicationDto> apps = applicationService.getAllApplications(token);
        return ResponseEntity.ok(new ResponseStructure<>("All applications retrieved successfully", apps));
    }

    // --- Delete Application ---
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<Void>> deleteApplication(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        log.info("Deleting application {}", id);
        applicationService.deleteApplication(id, token);
        return ResponseEntity.ok(new ResponseStructure<>("Application deleted successfully", null));
    }
}
