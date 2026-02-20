package com.CardMaster.service.cpl;

import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.dto.cpl.request.CardProductCreateRequest;
import com.CardMaster.dto.cpl.request.CardProductUpdateRequest;
import com.CardMaster.dto.cpl.response.CardProductResponse;
import com.CardMaster.dto.cpl.response.FeeConfigResponse;

import java.util.List;

public interface CardProductService {

    CardProductResponse createProduct(CardProductCreateRequest dto);

    CardProductResponse updateProduct(CardProductUpdateRequest dto);

    // Keeping this as entity list as per your earlier code; you can switch to DTO later.
    List<CardProduct> getAllProducts();

    FeeConfigResponse addFeeToProduct(Long productId, String type, Double amount);
}