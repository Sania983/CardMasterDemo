package com.CardMaster.controller.cau;

import com.CardMaster.dto.cau.CreditScoreGenerateRequest;
import com.CardMaster.dto.cau.CreditScoreResponse;
import com.CardMaster.dto.cau.UnderwritingDecisionRequest;
import com.CardMaster.dto.cau.UnderwritingDecisionResponse;
import com.CardMaster.mapper.cau.UnderwritingMapper;
import com.CardMaster.model.cau.CreditScore;
import com.CardMaster.model.cau.UnderwritingDecision;
import com.CardMaster.service.cau.UnderwritingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Module-3: Credit Assessment & Underwriting
 * Controller applies DTOs and calls mapper.toDto(...) for responses.
 * - Requests: DTOs
 * - Service: returns Entities
 * - Controller: converts Entities -> Response DTOs via MapStruct
 * - No URI/Location headers
 */
@RestController
@RequestMapping("/applications/{appId}")
public class UnderwritingController {

    private final UnderwritingService service;
    private final UnderwritingMapper mapper;

    public UnderwritingController(UnderwritingService service, UnderwritingMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Create (generate) a credit score for the application
     * POST /applications/{appId}/scores
     * Body: CreditScoreGenerateRequest (DTO)
     * Returns: CreditScoreResponse (DTO)
     */
    @PostMapping("/scores")
    public ResponseEntity<CreditScoreResponse> createScore(@PathVariable Long appId,
            @Valid @RequestBody CreditScoreGenerateRequest req) {

        CreditScore entity = service.generateScore(appId, req);
        CreditScoreResponse resDto = mapper.toCreditScoreResponse(entity); // entity -> dto
        return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
    }

    /**
     * Get the latest credit score for the application
     * GET /applications/{appId}/scores/latest
     * Returns: CreditScoreResponse (DTO)
     */
    @GetMapping("/scores/latest")
    public ResponseEntity<CreditScoreResponse> getLatestScore(@PathVariable Long appId) {
        CreditScore entity = service.getLatestScore(appId);
        CreditScoreResponse resDto = mapper.toCreditScoreResponse(entity); // entity -> dto
        return ResponseEntity.ok(resDto);
    }

    /**
     * Create an underwriting decision (auto-decide if 'decision' null)
     * POST /applications/{appId}/decisions
     * Body: UnderwritingDecisionRequest (DTO)
     * Returns: UnderwritingDecisionResponse (DTO)
     */
    @PostMapping("/decisions")
    public ResponseEntity<UnderwritingDecisionResponse> createDecision(
            @PathVariable Long appId,
            @Valid @RequestBody UnderwritingDecisionRequest req) {

        UnderwritingDecision entity = service.createDecision(appId, req);
        UnderwritingDecisionResponse resDto = mapper.toUnderwritingDecisionResponse(entity); // entity -> dto
        return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
    }

    /**
     * Get the latest underwriting decision for the application
     * GET /applications/{appId}/decisions/latest
     * Returns: UnderwritingDecisionResponse (DTO)
     */
    @GetMapping("/decisions/latest")
    public ResponseEntity<UnderwritingDecisionResponse> getLatestDecision(@PathVariable Long appId) {
        UnderwritingDecision entity = service.getLatestDecision(appId);
        UnderwritingDecisionResponse resDto = mapper.toUnderwritingDecisionResponse(entity); // entity -> dto
        return ResponseEntity.ok(resDto);
    }
}