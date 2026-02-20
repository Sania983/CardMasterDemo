
package com.CardMaster.mapper.cau;

import com.CardMaster.model.cau.CreditScore;
import com.CardMaster.model.cau.UnderwritingDecision;
import com.CardMaster.dto.cau.CreditScoreResponse;
import com.CardMaster.dto.cau.UnderwritingDecisionResponse;

public class UnderwritingMapper {

    public static CreditScoreResponse toScoreResponse(CreditScore e) {
        CreditScoreResponse d = new CreditScoreResponse();
        d.setScoreId(e.getScoreId());
        d.setApplicationId(e.getApplication().getApplicationId());
        d.setBureauScore(e.getBureauScore());
        d.setInternalScore(e.getInternalScore());
        d.setGeneratedDate(e.getGeneratedDate().toString());
        return d;
    }

    public static UnderwritingDecisionResponse toDecisionResponse(UnderwritingDecision e) {
        UnderwritingDecisionResponse d = new UnderwritingDecisionResponse();
        d.setDecisionId(e.getDecisionId());
        d.setApplicationId(e.getApplication().getApplicationId());
        d.setUnderwriterId(e.getUnderwriter().getUserId());
        d.setDecision(e.getDecision());
        d.setApprovedLimit(e.getApprovedLimit());
        d.setRemarks(e.getRemarks());
        d.setDecisionDate(e.getDecisionDate().toString());
        return d;
    }
}