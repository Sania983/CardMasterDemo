package com.CardMaster.service.cias;

import com.CardMaster.dao.cias.CardAccountRepository;
import com.CardMaster.exceptions.cias.AccountSetupException;
import com.CardMaster.model.cias.CardAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountSetupService {

    private final CardAccountRepository accountRepository;

    public List<CardAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    public CardAccount getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountSetupException("Account not found"));
    }
}
