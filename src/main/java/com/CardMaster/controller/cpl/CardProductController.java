package com.CardMaster.controller.cpl;

import com.CardMaster.dto.cpl.CardProductRequestDto;
import com.CardMaster.dto.cpl.CardProductResponseDto;
import com.CardMaster.service.cpl.CardProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class CardProductController {

    private final CardProductService service;

    @PostMapping
    public ResponseEntity<CardProductResponseDto> create(@RequestBody CardProductRequestDto request) {
        // Controller passes exactly 1 argument to service.create(...)
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CardProductResponseDto>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardProductResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}