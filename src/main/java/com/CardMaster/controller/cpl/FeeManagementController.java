package com.CardMaster.controller.cpl;

import com.CardMaster.dto.cpl.response.FeeConfigResponse;
import com.CardMaster.service.cpl.FeeConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/fees")
public class FeeManagementController {

    private final FeeConfigService feeConfigService;

    public FeeManagementController(FeeConfigService feeConfigService) {
        this.feeConfigService = feeConfigService;
    }

    // PATCH is semantically correct for partial field update
    @PatchMapping("/{feeId}/amount")
    public ResponseEntity<FeeConfigResponse> updateFeeAmount(
            @PathVariable Long feeId,
            @RequestParam BigDecimal amount
    ) {
        return ResponseEntity.ok(feeConfigService.updateAmount(feeId, amount));
    }
}