package com.CardMaster.controller.bsp;

import com.CardMaster.dto.bsp.StatementDto;
import com.CardMaster.mapper.bsp.StatementMapper;
import com.CardMaster.model.bsp.Statement;
import com.CardMaster.service.bsp.billing.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing/statements")
@RequiredArgsConstructor
public class StatementController {

    private final StatementService service;
    private final StatementMapper mapper;

    /**
     * Generate a statement for an account & period.
     * The service will compute totals (totalDue/minimumDue) based on posted transactions.
     */
    @PostMapping("/generate")
    public ResponseEntity<StatementDto> generate(@RequestBody StatementDto dto) {
        Statement input = mapper.toEntity(dto);
        Statement generated = service.generateStatement(input);
        return ResponseEntity.ok(mapper.toDTO(generated));
    }

    /**
     * Close a statement (e.g., after due date or when payment cycle ends).
     */
    @PostMapping("/close/{id}")
    public ResponseEntity<StatementDto> close(@PathVariable Long id) {
        Statement closed = service.closeStatement(id);
        return ResponseEntity.ok(mapper.toDTO(closed));
    }

    /**
     * Get a statement by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StatementDto> get(@PathVariable Long id) {
        Statement st = service.getById(id);
        return ResponseEntity.ok(mapper.toDTO(st));
    }

    /**
     * List all statements (basic; add filters later if needed).
     */
    @GetMapping
    public ResponseEntity<List<StatementDto>> list() {
        List<Statement> list = service.listAll();
        return ResponseEntity.ok(list.stream().map(mapper::toDTO).toList());
    }
}