package com.CardMaster.dao;

import com.CardMaster.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    // Query by status (your Card entity has a field named "status")
    List<Card> findByStatus(Card.Status status);

    // Example: query by customerId (also exists in Card entity)
    List<Card> findByCustomerId(Long customerId);
}
