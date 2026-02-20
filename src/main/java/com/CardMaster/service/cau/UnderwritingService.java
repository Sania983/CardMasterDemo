package com.CardMaster.service.cau;

import com.CardMaster.dao.CardApplicationRepository;
import com.CardMaster.dao.CreditScoreRepository;
import com.CardMaster.dao.UnderwritingDecisionRepository;
import com.CardMaster.dao.UserRepository1;
import com.CardMaster.dao.iam.UserRepository1;
import com.CardMaster.exception.ResourceNotFoundException;
import com.CardMaster.mapper.UnderwritingMapper;
import com.CardMaster.model.CardApplication;
import com.CardMaster.model.CreditScore;
import com.CardMaster.model.UnderwritingDecision;
import com.CardMaster.model.User;
import com.CardMaster.Enum.UnderwritingDecisionType;
import com.CardMaster.dto.CreditScoreGenerateRequest;
import com.CardMaster.dto.CreditScoreResponse;
import com.CardMaster.dto.UnderwritingDecisionRequest;
import com.CardMaster.dto.UnderwritingDecisionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnderwritingService {

    @Autowired
    private CreditScoreRepository scoreRepo;
    @Autowired
    private UnderwritingDecisionRepository decisionRepo;
    @Autowired
    private CardApplicationRepository appRepo;
    @Autowired
    private UserRepository1 userRepo;
    @Autowired
    private UnderwritingMapper mapper;

    public CreditScoreResponse generateScore(CreditScoreGenerateRequest req) {
        CardApplication app = appRepo.findById(req.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + req.getApplicationId()));

        // request -> entity
        CreditScore cs = mapper.fromRequest(req, app);

        // save -> entity
        cs = scoreRepo.save(cs);

        // entity -> response
        return mapper.toDto(cs);
    }

    public CreditScoreResponse getLatestScore(Long applicationId) {
        CreditScore cs = scoreRepo
                .findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found for ApplicationID: " + applicationId));
        return mapper.toDto(cs);
    }

    // -------- DECISION --------
    public UnderwritingDecisionResponse createDecision(UnderwritingDecisionRequest req) {
        CardApplication app = appRepo.findById(req.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + req.getApplicationId()));

        User underwriter = userRepo.findById(req.getUnderwriterId())
                .orElseThrow(() -> new ResourceNotFoundException("Underwriter not found: " + req.getUnderwriterId()));

        // request -> entity
        UnderwritingDecision dec = mapper.fromRequest(req, app, underwriter);

        // if decision not provided, auto-decide using latest score
        if (dec.getDecision() == null) {
            CreditScore latest = scoreRepo
                    .findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(req.getApplicationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Generate score first for application: " + req.getApplicationId()));
            int internal = latest.getInternalScore();
            if (internal >= 70) dec.setDecision(UnderwritingDecisionType.APPROVE);
            else if (internal >= 50) dec.setDecision(UnderwritingDecisionType.CONDITIONAL);
            else dec.setDecision(UnderwritingDecisionType.REJECT);
        }

        // save -> entity
        dec = decisionRepo.save(dec);

        // entity -> response
        return mapper.toDto(dec);
    }

    public UnderwritingDecisionResponse getLatestDecision(Long applicationId) {
        UnderwritingDecision dec = decisionRepo
                .findTopByApplication_ApplicationIdOrderByDecisionDateDesc(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision not found for ApplicationID: " + applicationId));
        return mapper.toDto(dec);
    }
}