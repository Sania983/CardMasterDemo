package com.CardMaster.controller.cias;

import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.service.cias.AccountSetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountSetupController {

    private final AccountSetupService accountSetupService;

    // Get all accounts
    @GetMapping
    public ResponseEntity<List<CardAccount>> getAllAccounts() {
        return ResponseEntity.ok(accountSetupService.getAllAccounts());
    }

    // Get account by ID
    @GetMapping("/{id}")
    public ResponseEntity<CardAccount> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountSetupService.getAccountById(id));
    }
}
