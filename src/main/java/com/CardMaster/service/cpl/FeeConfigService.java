// src/main/java/com/CardMaster/service/FeeConfigService.java
package com.CardMaster.service.cpl;

import com.CardMaster.model.cpl.FeeConfig;
import com.CardMaster.mapper.cpl.FeeConfigMapper;
import com.CardMaster.dao.cpl.FeeConfigRepository;
import com.CardMaster.dto.cpl.response.FeeConfigResponse;
import com.CardMaster.exception.cpl.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class FeeConfigService {

    private final FeeConfigRepository feeRepo;
    private final FeeConfigMapper mapper;

    public FeeConfigService(FeeConfigRepository feeRepo, FeeConfigMapper mapper) {
        this.feeRepo = feeRepo;        // ✅ you forgot this line earlier
        this.mapper = mapper;
    }

    /** Update only the amount (immutable style: rebuild and save) */
    @Transactional
    public FeeConfigResponse updateAmount(Long feeId, BigDecimal newAmount) {
        FeeConfig fee = feeRepo.findById(feeId)
                .orElseThrow(() -> new NotFoundException("FeeConfig not found")); // ✅ one-arg ctor

        // Rebuild immutable entity with updated amount (no setters)
        FeeConfig updated = new FeeConfig(
                fee.feeId(),      // same PK
                fee.product(),    // same relation
                fee.feeType(),    // same enum
                newAmount         // new amount
        );

        return mapper.toResponse(feeRepo.save(updated));
    }
}