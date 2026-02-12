package com.CardMaster.controller;

import com.CardMaster.model.CardAccount;
import com.CardMaster.service.AccountSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountSetupController {

    @Autowired
    private AccountSetupService accountSetupService;

    @PostMapping("/create")
    public CardAccount createAccount(@RequestBody CardAccount account) {
        return accountSetupService.createAccount(account);
    }

    @GetMapping
    public List<CardAccount> getAllAccounts() {
        return accountSetupService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public CardAccount getAccountById(@PathVariable Long id) {
        return accountSetupService.getAccountById(id);
    }


}
