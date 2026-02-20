package com.CardMaster.service.cias;

import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.dao.cias.CardAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AccountSetupService {

    @Autowired
    private CardAccountRepository accountRepository;

    // Create a new account
    public CardAccount createAccount(CardAccount account) {
        account.setAvailableLimit(account.getCreditLimit());
        account.setOpenDate(LocalDate.now());
        account.setStatus(CardAccount.Status.Active);
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
