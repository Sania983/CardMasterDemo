package com.CardMaster.dao.tap;

import com.CardMaster.model.tap.TransactionHold;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionHoldRepository extends JpaRepository<TransactionHold, Long> {}
