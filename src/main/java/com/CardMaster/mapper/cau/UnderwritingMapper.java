package com.CardMaster.mapper.cau;

import com.CardMaster.dto.cau.CreditScoreGenerateRequest;
import com.CardMaster.dto.cau.CreditScoreResponse;
import com.CardMaster.dto.cau.UnderwritingDecisionRequest;
import com.CardMaster.dto.cau.UnderwritingDecisionResponse;

import com.CardMaster.model.cau.CreditScore;
import com.CardMaster.model.cau.UnderwritingDecision;
import com.CardMaster.model.iam.User;
import com.CardMaster.model.paa.CardApplication;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UnderwritingMapper {

    // ----------------------------------------------------------
    // Convert CreditScoreGenerateRequest -> CreditScore Entity
    // ----------------------------------------------------------
    public CreditScore fromCreditScoreRequest(CreditScoreGenerateRequest req,
                                              CardApplication app) {
        CreditScore cs = new CreditScore();
        cs.setApplication(app);
        cs.setBureauScore(req.getBureauScore());

        // simple internal score logic (demo only)
        cs.setInternalScore(req.getBureauScore() / 10);

        cs.setGeneratedDate(LocalDateTime.now());
        return cs;
    }

    // ----------------------------------------------------------
    // Convert Entity -> CreditScoreResponse (Controller Uses)
    // ----------------------------------------------------------
    public CreditScoreResponse toCreditScoreResponse(CreditScore e) {
        CreditScoreResponse d = new CreditScoreResponse();

        d.setScoreId(e.getScoreId());
        d.setApplicationId(e.getApplication().getApplicationId());
        d.setBureauScore(e.getBureauScore());
        d.setInternalScore(e.getInternalScore());
        d.setGeneratedDate(e.getGeneratedDate().toString());

        return d;
    }

    // ----------------------------------------------------------
    // Convert UnderwritingDecisionRequest -> Entity
    // ----------------------------------------------------------
    public UnderwritingDecision fromUnderwritingDecisionRequest(
            UnderwritingDecisionRequest req,
            CardApplication app,
            User underwriter) {

        UnderwritingDecision dec = new UnderwritingDecision();
        dec.setApplicationid(app);
        dec.setUnderwriterid(underwriter);

        dec.setDecision(req.getDecision());   // can be null (auto-decide in service)
        dec.setApprovedLimit(req.getApprovedLimit());
        dec.setRemarks(req.getRemarks());
        dec.setDecisionDate(LocalDateTime.now());

        return dec;
    }

    // ----------------------------------------------------------
    // Convert Entity -> UnderwritingDecisionResponse
    // Controller calls -> mapper.toUnderwritingDecisionResponse()
    // ----------------------------------------------------------
    public UnderwritingDecisionResponse toUnderwritingDecisionResponse(UnderwritingDecision e) {

        UnderwritingDecisionResponse d = new UnderwritingDecisionResponse();

        d.setDecisionId(e.getDecisionId());
        d.setApplicationId(e.getApplicationid().getApplicationId());
        d.setUnderwriterId(e.getUnderwriterid().getUserId());
        d.setDecision(e.getDecision());
        d.setApprovedLimit(e.getApprovedLimit());
        d.setRemarks(e.getRemarks());
        d.setDecisionDate(e.getDecisionDate().toString());

        return d;
    }
}
