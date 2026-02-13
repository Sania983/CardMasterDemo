package com.CardMaster.controller.cau;

import com.CardMaster.dao.cau.CardApplicationRepository;
import com.CardMaster.dao.cau.UserRepository;
import com.CardMaster.service.cau.UnderwritingService;
import com.CardMaster.model.cau.CardApplication;
import com.CardMaster.model.cau.User;
import com.CardMaster.dto.cau.CreditScoreGenerateRequest;
import com.CardMaster.dto.cau.CreditScoreResponse;
import com.CardMaster.dto.cau.UnderwritingDecisionRequest;
import com.CardMaster.dto.cau.UnderwritingDecisionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/underwriting") // simple base path
public class UnderwritingController {

    @Autowired
    private UnderwritingService service;
    @Autowired
    private CardApplicationRepository applicationRepo;

    @Autowired
    private UserRepository userRepo;
    // 1) Create/Generate Credit Score
    @PostMapping("/score")
    public CreditScoreResponse generateScore(@RequestBody CreditScoreGenerateRequest req) {
        return service.generateScore(req);
    }

    // 2) Get Latest Credit Score by Application
    @GetMapping("/score/{appId}")
    public CreditScoreResponse getLatestScore(@PathVariable Long appId) {
        return service.getLatestScore(appId);
    }

    // 3) Create Underwriting Decision (auto if decision not provided)
    @PostMapping("/decision")
    public UnderwritingDecisionResponse createDecision(@RequestBody UnderwritingDecisionRequest req) {
        return service.createDecision(req);
    }

    // 4) Get Latest Underwriting Decision by Application
    @GetMapping("/decision/{appId}")
    public UnderwritingDecisionResponse getLatestDecision(@PathVariable Long appId) {
        return service.getLatestDecision(appId);
    }

    @PostMapping("/create-app/{limit}")
    public Long createApplication(@PathVariable double limit) {
        CardApplication app = new CardApplication();
        app.setRequestedLimit(limit); // minimal field so it's not empty
        app = applicationRepo.save(app);
        return app.getApplicationId();
    }


    @PostMapping("/create-user/{name}")
    public Long createUnderwriter(@PathVariable String name) {
        User u = new User();
        u.setName(name);
        u = userRepo.save(u);
        return u.getUserId();
    }
}