package com.CardMaster.dao.bsp;

import com.CardMaster.model.bsp.Statement;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StatementRepository extends JpaRepository<Statement, Long> {}
