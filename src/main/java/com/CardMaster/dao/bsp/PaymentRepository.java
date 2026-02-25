package com.CardMaster.dao.bsp;


import com.CardMaster.model.billing.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}
