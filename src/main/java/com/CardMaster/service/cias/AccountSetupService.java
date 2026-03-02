package com.CardMaster.service.cias;

import com.CardMaster.Enum.cias.AccountStatus;
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
    private final JwtUtil jwtUtil;

    // Create a new account with JWT validation
    public CardAccount createAccount(CardAccount account,String token) {
        // Validate JWT
        jwtUtil.extractUsername(token.substring(7));

        if (account.getCreditLimit() == null || account.getCreditLimit() <= 0) {
            throw new AccountSetupException("Credit limit must be provided and positive");
        }
        account.setAvailableLimit(account.getCreditLimit());
        account.setOpenDate(LocalDate.now());
        account.setStatus(AccountStatus.ACTIVE);
        return accountRepository.save(account);
    }

    // Fetch all accounts
    public List<CardAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Fetch account by ID
    public CardAccount getAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }
}