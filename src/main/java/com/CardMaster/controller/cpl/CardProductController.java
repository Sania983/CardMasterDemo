package com.CardMaster.controller.cpl;

import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.mapper.cpl.CardProductMapper;
import com.CardMaster.dto.cpl.request.CardProductCreateRequest;
import com.CardMaster.dto.cpl.request.CardProductUpdateRequest;
import com.CardMaster.dto.cpl.response.CardProductResponse;
import com.CardMaster.dto.cpl.response.FeeConfigResponse;
import com.CardMaster.service.cpl.CardProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class CardProductController {


    private final CardProductService service;
    private final CardProductMapper productMapper;

    public CardProductController(CardProductService service, CardProductMapper productMapper) {
        this.service = service;
        this.productMapper = productMapper;
    }


    // CREATE product - send DTO; receive DTO
    @PostMapping
    public ResponseEntity<CardProductResponse> create(@RequestBody @Valid CardProductCreateRequest product) {
        return ResponseEntity.ok(service.createProduct(product));
    }

    // UPDATE product - send DTO; receive DTO (optional but recommended)
    @PutMapping
    public ResponseEntity<CardProductResponse> update(@RequestBody @Valid CardProductUpdateRequest product) {
        return ResponseEntity.ok(service.updateProduct(product));
    }

    // LIST products - you kept this as entity list; that's OK for now

    @GetMapping
    public ResponseEntity<List<CardProductResponse>> getAll() {
        var entities = service.getAllProducts();       // List<CardProduct>
        var dtos = productMapper.toResponses(entities);
        return ResponseEntity.ok(dtos);
    }


    // ADD fee to a product - return DTO response
    @PostMapping("/{id}/fees")
    public ResponseEntity<FeeConfigResponse> addFee(
            @PathVariable Long id,
            @RequestParam String type,
            @RequestParam Double amount
    ) {
        return ResponseEntity.ok(service.addFeeToProduct(id, type, amount));
    }
}