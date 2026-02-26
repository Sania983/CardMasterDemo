package com.CardMaster.dao.bsp;


import com.CardMaster.model.bsp.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}
