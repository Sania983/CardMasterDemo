package com.CardMaster.service.cpl;

import com.CardMaster.model.cpl.FeeConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FeeConfigService {

    FeeConfig create(FeeConfig entity);

    List<FeeConfig> findAll();

    Optional<FeeConfig> findById(Long id);

    /**
     * Updates only the amount for the given fee config.
     * Returns the updated entity.
     */
    FeeConfig updateAmount(Long id, BigDecimal newAmount);

    void deleteById(Long id);
}