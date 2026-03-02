package com.CardMaster.service.cias;

import com.CardMaster.Enum.cias.AccountStatus;
import com.CardMaster.Enum.cias.CardStatus;
import com.CardMaster.dao.cias.CardRepository;
import com.CardMaster.dto.cias.CardAccountRequestDto;
import com.CardMaster.dto.cias.CardAccountResponseDto;
import com.CardMaster.mapper.cias.CardAccountMapper;
import com.CardMaster.model.cias.Card;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.dao.cias.CardAccountRepository;
import com.CardMaster.security.iam.JwtUtil; // assuming you have this utility
import lombok.RequiredArgsConstructor;
import com.CardMaster.exceptions.cias.AccountSetupException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountSetupService {

    private final CardAccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final CardAccountMapper accountMapper;

    public CardAccount createAccount(CardAccountRequestDto requestDto) {
        CardAccount account = accountMapper.toEntity(requestDto);

        // After account creation, card becomes ACTIVE
        Card card = account.getCard();
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);

        return accountRepository.save(account);
    }

    public CardAccount getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
    }
}
