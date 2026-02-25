package com.CardMaster.service.cau;

import com.CardMaster.dao.cau.CreditScoreRepository;
import com.CardMaster.dao.cau.UnderwritingDecisionRepository;
import com.CardMaster.dao.iam.UserRepository;
import com.CardMaster.dao.paa.CardApplicationRepository;
import com.CardMaster.dto.cau.CreditScoreGenerateRequest;
import com.CardMaster.dto.cau.UnderwritingDecisionRequest;
import com.CardMaster.exceptions.cau.EntityNotFoundException;
import com.CardMaster.exceptions.cau.ValidationException;
import com.CardMaster.model.cau.CreditScore;
import com.CardMaster.model.cau.UnderwritingDecision;
import com.CardMaster.model.iam.User;
import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.Enum.cau.UnderwritingDecisionType;
import com.CardMaster.model.paa.CardApplication.CardApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class UnderwritingService {
    private final CreditScoreRepository creditScoreRepository;
    private final UnderwritingDecisionRepository decisionRepository;
    private final CardApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    // -------------------------------------------------------
    // 1) CREATE CREDIT SCORE (internalScore is derived here)
    // -------------------------------------------------------
    public CreditScore generateScore(Long appId, CreditScoreGenerateRequest req) {

        // Fetch application
        CardApplication app = applicationRepository.findById(appId)
                .orElseThrow(() -> new EntityNotFoundException("CardApplication", appId));

        // Validate
        if (req.getBureauScore() == null || req.getBureauScore() <= 0) {
            throw new ValidationException("Bureau score must be positive");
        }

        // Derive internalScore from bureauScore (Option A)
        int internalScore = req.getBureauScore() / 10;

        // Create and save score
        CreditScore score = CreditScore.builder()
                .application(app)
                .bureauScore(req.getBureauScore())
                .internalScore(internalScore)
                .generatedDate(LocalDateTime.now())
                .build();

        return creditScoreRepository.save(score);
    }

    // -------------------------------------------------------
    // 2) GET LATEST CREDIT SCORE
    // -------------------------------------------------------
    public CreditScore getLatestScore(Long appId) {
        // Your CreditScore entity field is "application", so this repo method name is correct.
        return creditScoreRepository
                .findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(appId)
                .orElseThrow(() -> new EntityNotFoundException("Latest CreditScore for application", appId));
    }

    // -------------------------------------------------------
    // 3) CREATE UNDERWRITING DECISION
    //    - If request.decision is null, auto-decide using simple rules
    // -------------------------------------------------------
    public UnderwritingDecision createDecision(Long appId, UnderwritingDecisionRequest req) {

        // Fetch application and underwriter
        CardApplication app = applicationRepository.findById(appId)
                .orElseThrow(() -> new EntityNotFoundException("CardApplication", appId));

        if (req.getUnderwriterId() == null) {
            throw new ValidationException("Underwriter id is required");
        }

        User underwriter = userRepository.findById(req.getUnderwriterId())
                .orElseThrow(() -> new EntityNotFoundException("User", req.getUnderwriterId()));

        // Determine decision and limit
        UnderwritingDecisionType finalDecision = req.getDecision();
        Double finalLimit = req.getApprovedLimit();

        if (finalDecision == null) {
            // Need the latest score to auto-decide
            CreditScore score = creditScoreRepository
                    .findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(appId)
                    .orElseThrow(() -> new ValidationException("Generate Credit Score first"));

            // super-simple, explainable rule:
            // >=750 -> APPROVE (full requested limit)
            // <600  -> REJECT (0 limit)
            // else  -> CONDITIONAL (50% of requested)
            if (score.getBureauScore() >= 750) {
                finalDecision = UnderwritingDecisionType.APPROVE;
                finalLimit = app.getRequestedLimit();
            } else if (score.getBureauScore() < 600) {
                finalDecision = UnderwritingDecisionType.REJECT;
                finalLimit = 0.0;
            } else {
                finalDecision = UnderwritingDecisionType.CONDITIONAL;
                finalLimit = (app.getRequestedLimit() != null) ? app.getRequestedLimit() * 0.5 : 0.0;
            }
        } else {
            // Manual decision provided: validate limit for APPROVE/CONDITIONAL
            if ((finalDecision == UnderwritingDecisionType.APPROVE ||
                    finalDecision == UnderwritingDecisionType.CONDITIONAL)) {

                if (finalLimit == null || finalLimit <= 0) {
                    throw new ValidationException("Approved limit must be positive for APPROVE/CONDITIONAL");
                }
            }
            // For REJECT, limit can be 0 or null (we normalize to 0)
            if (finalDecision == UnderwritingDecisionType.REJECT) {
                finalLimit = 0.0;
            }
        }

        // Build and save decision
        UnderwritingDecision decision = UnderwritingDecision.builder()
                .applicationid(app)        // note: your entity field is "applicationid"
                .underwriterid(underwriter)
                .decision(finalDecision)
                .approvedLimit(finalLimit)
                .remarks(req.getRemarks())
                .decisionDate(LocalDateTime.now())
                .build();

        decisionRepository.save(decision);

        // Update application status based on decision
        if (finalDecision == UnderwritingDecisionType.APPROVE) {
            app.setStatus(CardApplicationStatus.Approved);
        } else if (finalDecision == UnderwritingDecisionType.REJECT) {
            app.setStatus(CardApplicationStatus.Rejected);
        } else {
            app.setStatus(CardApplicationStatus.UnderReview);
        }
        applicationRepository.save(app);

        return decision;
    }

    // -------------------------------------------------------
    // 4) GET LATEST UNDERWRITING DECISION
    // -------------------------------------------------------
    public UnderwritingDecision getLatestDecision(Long appId) {
        // IMPORTANT:
        // Your UnderwritingDecision entity field name is "applicationid" (not "application").
        // So your repository method should be:
        //   findTopByApplicationid_ApplicationIdOrderByDecisionDateDesc(Long applicationId)
        //
        // If your current repository uses "Application_" in method name, please rename it to match the field.
        return decisionRepository
                .findTopByApplicationid_ApplicationIdOrderByDecisionDateDesc(appId)
                .orElseThrow(() -> new EntityNotFoundException("Latest UnderwritingDecision", appId));
    }
}