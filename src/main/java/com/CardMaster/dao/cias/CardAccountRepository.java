package com.CardMaster.dao.cias;

import com.CardMaster.model.cias.CardAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardAccountRepository extends JpaRepository<CardAccount, Long> {

    // Query by accountId (this field exists in CardAccount)
    Optional<CardAccount> findByAccountId(Long accountId);

    // Query by cardId (this field also exists in CardAccount)
    Optional<CardAccount> findByCardId(Long cardId);
}
