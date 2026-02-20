package com.CardMaster.controller.cpl;

import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.service.cpl.CardProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class CardProductController {

    private final CardProductService productService;

    @PostMapping
    public ResponseEntity<CardProduct> create(@Valid @RequestBody CardProduct product) {
        return ResponseEntity.ok(productService.create(product));
    }

    @GetMapping
    public ResponseEntity<List<CardProduct>> list() {
        return ResponseEntity.ok(productService.findAll());
    }
}