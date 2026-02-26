package com.CardMaster.dao.tap;

import com.CardMaster.model.tap.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {}