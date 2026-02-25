package com.CardMaster.dao.tap;

import com.CardMaster.model.transactions.TransactionHold;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHoldRepository extends JpaRepository<TransactionHold, Long> {}
