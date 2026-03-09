package com.CardMaster.service.cias;

import com.CardMaster.Enum.cias.AccountStatus;
import com.CardMaster.Enum.cias.CardStatus;
import com.CardMaster.dao.cias.CardRepository;
import com.CardMaster.dao.cias.CardAccountRepository;
import com.CardMaster.dto.cias.CardAccountRequestDto;
import com.CardMaster.mapper.cias.CardAccountMapper;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cias.CardAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AccountSetupService {

    private final CardAccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final CardAccountMapper accountMapper;

    public CardAccount createAccount(CardAccountRequestDto requestDto) {
        Card card = cardRepository.findById(requestDto.getCardId())
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + requestDto.getCardId()));

        if (card.getStatus() != CardStatus.ISSUED) {
            throw new IllegalStateException("Card must be ISSUED before linking to an account");
        }

        CardAccount account = new CardAccount();
        account.setCard(card);
        account.setCreditLimit(requestDto.getCreditLimit());
        account.setAvailableLimit(requestDto.getCreditLimit());
        account.setOpenDate(LocalDate.now()); // auto-set today's date
        account.setStatus(AccountStatus.ACTIVE);

        // activate card
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);

        return accountRepository.save(account);
    }

    public CardAccount getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
    }

    public CardAccount useCard(Long accountId, Double amount) {
        CardAccount account = getAccountById(accountId);

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (account.getAvailableLimit() < amount) {
            throw new IllegalStateException("Insufficient available limit");
        }

        account.setAvailableLimit(account.getAvailableLimit() - amount);
        return accountRepository.save(account);
    }
}
