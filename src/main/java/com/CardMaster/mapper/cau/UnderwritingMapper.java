package com.CardMaster.mapper.cau;

import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.model.cau.CreditScore;
import com.CardMaster.model.cau.UnderwritingDecision;
import com.CardMaster.model.iam.User;
import com.CardMaster.dto.cau.CreditScoreGenerateRequest;
import com.CardMaster.dto.cau.CreditScoreResponse;
import com.CardMaster.dto.cau.UnderwritingDecisionRequest;
import com.CardMaster.dto.cau.UnderwritingDecisionResponse;
import com.CardMaster.model.paa.CardApplication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UnderwritingMapper {

    public CreditScore fromRequest(CreditScoreGenerateRequest req, CardApplication app) {
        CreditScore cs = new CreditScore();
        cs.setApplication(app);
        cs.setBureauScore(req.getBureauScore());
        cs.setInternalScore(req.getBureauScore() / 10);
        cs.setGeneratedDate(LocalDateTime.now());
        return cs;
    }
    public CreditScoreResponse toDto(CreditScore e) {
        CreditScoreResponse d = new CreditScoreResponse();
        d.setScoreId(e.getScoreId());
        d.setApplicationId(e.getApplication().getApplicationId());
        d.setBureauScore(e.getBureauScore());
        d.setInternalScore(e.getInternalScore());
        d.setGeneratedDate(e.getGeneratedDate().toString());
        return d;
    }
    public UnderwritingDecision fromRequest(UnderwritingDecisionRequest req,
                                            CardApplication app,
                                            User underwriter) {
        UnderwritingDecision dec = new UnderwritingDecision();
        dec.setApplicationid(app);
        dec.setUnderwriterid(underwriter);
        dec.setDecision(req.getDecision());         // may be null (auto-decide in service)
        dec.setApprovedLimit(req.getApprovedLimit());
        dec.setRemarks(req.getRemarks());
        dec.setDecisionDate(LocalDateTime.now());
        return dec;
    }

    // Entity -> Response DTO
    public UnderwritingDecisionResponse toDto(UnderwritingDecision e) {
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