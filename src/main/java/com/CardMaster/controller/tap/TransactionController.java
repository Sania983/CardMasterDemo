package com.CardMaster.controller.tap;

import com.CardMaster.dto.tap.TransactionDto;
import com.CardMaster.mapper.tap.TransactionMapper;
import com.CardMaster.model.tap.Transaction;
import com.CardMaster.service.tap.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;
    private final TransactionMapper mapper;

    /**
     * Authorize a new transaction (creates a hold and sets status=AUTHORIZED).
     * Accepts TransactionDto to keep API decoupled from JPA entity.
     */
    @PostMapping("/authorize")
    public ResponseEntity<TransactionDto> authorize(@RequestBody TransactionDto dto) {
        Transaction entity = mapper.toEntity(dto);
        Transaction saved = service.authorize(entity);
        return ResponseEntity.ok(mapper.toDTO(saved));
    }

    /**
     * Post (capture) a previously authorized transaction by its ID.
     */
    @PostMapping("/post/{id}")
    public ResponseEntity<Transaction> post(@PathVariable Long id) {
        TransactionDto posted = service.post(id);
        return ResponseEntity.ok(mapper.toEntity(posted));
    }

    /**
     * Reverse a transaction (void an auth or reverse a posted transaction).
     */
    @PostMapping("/reverse/{id}")
    public ResponseEntity<TransactionDto> reverse(@PathVariable Long id) {
        Transaction reversed = service.reverse(id);
        return ResponseEntity.ok(mapper.toDTO(reversed));
    }

    /**
     * Get a transaction by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> get(@PathVariable Long id) {
        Transaction tx = service.getById(id);
        return ResponseEntity.ok(mapper.toDTO(tx));
    }

    /**
     * List all transactions (basic, no filters for now).
     */
    @GetMapping
    public ResponseEntity<List<TransactionDto>> list() {
        List<Transaction> all = service.listAll();
        return ResponseEntity.ok(all.stream().map(mapper::toDTO).toList());
    }
}