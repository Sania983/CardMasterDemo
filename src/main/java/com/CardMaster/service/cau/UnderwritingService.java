package com.CardMaster.service.cau;

import com.CardMaster.dao.cau.CreditScoreRepository;
import com.CardMaster.dao.cau.UnderwritingDecisionRepository;
import com.CardMaster.dao.cau.UserRepository;
import com.CardMaster.dao.paa.CardApplicationRepository;
import com.CardMaster.dto.cau.CreditScoreGenerateRequest;
import com.CardMaster.dto.cau.CreditScoreResponse;
import com.CardMaster.dto.cau.UnderwritingDecisionRequest;
import com.CardMaster.dto.cau.UnderwritingDecisionResponse;
import com.CardMaster.exceptions.cau.ResourceNotFoundException;
import com.CardMaster.mapper.cau.UnderwritingMapper;
import com.CardMaster.Enum.cau.UnderwritingDecisionType;
import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.model.cau.CreditScore;
import com.CardMaster.model.cau.UnderwritingDecision;
import com.CardMaster.model.cau.UserCau;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UnderwritingService {

    private final CreditScoreRepository scoreRepo;
    private final UnderwritingDecisionRepository decisionRepo;
    private final CardApplicationRepository appRepo;
    private final UserRepository userRepo;


    public UnderwritingService(CreditScoreRepository scoreRepo,
                               UnderwritingDecisionRepository decisionRepo,
                               CardApplicationRepository appRepo,
                               UserRepository userRepo) {
        this.scoreRepo = scoreRepo;
        this.decisionRepo = decisionRepo;
        this.appRepo = appRepo;
        this.userRepo = userRepo;
    }

    public CreditScoreResponse generateScore(CreditScoreGenerateRequest req) {
        CardApplication app = appRepo.findById(req.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + req.getApplicationId()));

        CreditScore cs = new CreditScore();
        cs.setApplication(app);
        cs.setBureauScore(req.getBureauScore());
        cs.setInternalScore(req.getBureauScore() / 10);
        cs.setGeneratedDate(LocalDateTime.now());

        return UnderwritingMapper.toScoreResponse(scoreRepo.save(cs));
    }

    public CreditScoreResponse getLatestScore(Long applicationId) {
        CreditScore cs = scoreRepo
                .findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found for ApplicationID: " + applicationId));
        return UnderwritingMapper.toScoreResponse(cs);
    }

    public UnderwritingDecisionResponse createDecision(UnderwritingDecisionRequest req) {
        CardApplication app = appRepo.findById(req.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + req.getApplicationId()));

        UserCau underwriter = userRepo.findById(req.getUnderwriterId())
                .orElseThrow(() -> new ResourceNotFoundException("Underwriter not found: " + req.getUnderwriterId()));

        UnderwritingDecisionType finalDecision = req.getDecision();
        if (finalDecision == null) {
            CreditScore latest = scoreRepo
                    .findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(req.getApplicationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Generate score first"));
            int internal = latest.getInternalScore();
            if (internal >= 70) finalDecision = UnderwritingDecisionType.APPROVE;
            else if (internal >= 50) finalDecision = UnderwritingDecisionType.CONDITIONAL;
            else finalDecision = UnderwritingDecisionType.REJECT;
        }

        UnderwritingDecision dec = new UnderwritingDecision();
        dec.setApplication(app);
        dec.setUnderwriter(underwriter);
        dec.setDecision(finalDecision);
        dec.setApprovedLimit(req.getApprovedLimit());
        dec.setRemarks(req.getRemarks());
        dec.setDecisionDate(LocalDateTime.now());

        return UnderwritingMapper.toDecisionResponse(decisionRepo.save(dec));
    }

    public UnderwritingDecisionResponse getLatestDecision(Long applicationId) {
        UnderwritingDecision dec = decisionRepo
                .findTopByApplication_ApplicationIdOrderByDecisionDateDesc(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Decision not found for ApplicationID: " + applicationId));
        return UnderwritingMapper.toDecisionResponse(dec);
    }
}
 