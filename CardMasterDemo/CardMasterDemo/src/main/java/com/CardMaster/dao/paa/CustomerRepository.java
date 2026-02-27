package com.CardMaster.dao.paa;

import com.CardMaster.model.paa.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
