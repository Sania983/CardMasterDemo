package com.CardMaster.dao.cpl;

import com.CardMaster.model.cpl.FeeConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface FeeConfigRepository extends JpaRepository<FeeConfig, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update FeeConfig f set f.amount = :amount where f.feeId = :feeId")
    int updateAmountById(@Param("feeId") Long feeId, @Param("amount") BigDecimal amount);
}