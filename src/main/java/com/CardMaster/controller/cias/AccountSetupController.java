package com.CardMaster.controller.cias;

import com.CardMaster.dto.cias.CardAccountRequestDto;
import com.CardMaster.dto.cias.CardAccountResponseDto;
import com.CardMaster.mapper.cias.CardAccountMapper;
import com.CardMaster.model.cias.CardAccount;
import com.CardMaster.service.cias.AccountSetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountSetupController {

    private final AccountSetupService accountService;
    private final CardAccountMapper accountMapper;

    @PostMapping
    public ResponseEntity<CardAccountResponseDto> createAccount(@RequestBody CardAccountRequestDto request) {
        CardAccount account = accountService.createAccount(request);
        return ResponseEntity.ok(accountMapper.toDTO(account));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<CardAccountResponseDto> getAccount(@PathVariable Long accountId) {
        CardAccount account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(accountMapper.toDTO(account));
    }
}
