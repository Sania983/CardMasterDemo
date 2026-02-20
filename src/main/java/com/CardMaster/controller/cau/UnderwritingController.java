package com.CardMaster.controller.cau;

import com.CardMaster.dao.cau.UserRepository;
import com.CardMaster.dao.paa.CardApplicationRepository;
import com.CardMaster.service.cau.UnderwritingService;
import com.CardMaster.model.cau.UserCau;
import com.CardMaster.dto.cau.CreditScoreGenerateRequest;
import com.CardMaster.dto.cau.CreditScoreResponse;
import com.CardMaster.dto.cau.UnderwritingDecisionRequest;
import com.CardMaster.dto.cau.UnderwritingDecisionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/underwriting")
public class UnderwritingController {

    @Autowired
    private UnderwritingService service;

    @Autowired
    private CardApplicationRepository applicationRepo;  // from PAA

    @Autowired
    private UserRepository userRepo;

    /**
     * Generate a credit score for an application
     */
    @PostMapping("/score")
    public CreditScoreResponse generateScore(@RequestBody CreditScoreGenerateRequest req) {
        return service.generateScore(req);
    }

    /**
     * Get the latest credit score for an application
     */
    @GetMapping("/score/{appId}")
    public CreditScoreResponse getLatestScore(@PathVariable Long appId) {
        return service.getLatestScore(appId);
    }

    /**
     * Create an underwriting decision (auto if decision not provided)
     */
    @PostMapping("/decision")
    public UnderwritingDecisionResponse createDecision(@RequestBody UnderwritingDecisionRequest req) {
        return service.createDecision(req);
    }

    /**
     * Get the latest underwriting decision for an application
     */
    @GetMapping("/decision/{appId}")
    public UnderwritingDecisionResponse getLatestDecision(@PathVariable Long appId) {
        return service.getLatestDecision(appId);
    }

    /**
     * Create a new underwriter user
     */
    @PostMapping("/create-user/{name}")
    public Long createUnderwriter(@PathVariable String name) {
        UserCau u = new UserCau();
        u.setName(name);
        u = userRepo.save(u);
        return u.getUserId();
    }
}
