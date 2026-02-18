package com.CardMaster.service.cpl;

import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.model.cpl.FeeConfig;
import com.CardMaster.Enum.cpl.FeeType;
import com.CardMaster.exception.cpl.NotFoundException;
import com.CardMaster.mapper.cpl.CardProductMapper;
import com.CardMaster.mapper.cpl.FeeConfigMapper;
import com.CardMaster.dao.cpl.CardProductRepository;
import com.CardMaster.dao.cpl.FeeConfigRepository;
import com.CardMaster.dto.cpl.request.CardProductCreateRequest;
import com.CardMaster.dto.cpl.request.CardProductUpdateRequest;
import com.CardMaster.dto.cpl.response.CardProductResponse;
import com.CardMaster.dto.cpl.response.FeeConfigResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class CardProductServiceImpl implements CardProductService {

    private final CardProductRepository productRepo;
    private final FeeConfigRepository feeRepo;
    private final CardProductMapper productMapper;
    private final FeeConfigMapper feeMapper;

    public CardProductServiceImpl(CardProductRepository productRepo,
                                  FeeConfigRepository feeRepo,
                                  CardProductMapper productMapper,
                                  FeeConfigMapper feeMapper) {
        this.productRepo = productRepo;
        this.feeRepo = feeRepo;
        this.productMapper = productMapper;
        this.feeMapper = feeMapper;
    }

    @Transactional
    @Override
    public CardProductResponse createProduct(CardProductCreateRequest dto) {
        var saved = productRepo.save(productMapper.toEntity(dto));
        return productMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public CardProductResponse updateProduct(CardProductUpdateRequest dto) {
        productRepo.findById(dto.productId())
                .orElseThrow(() -> new NotFoundException("Card product not found")); // one-arg ctor

        var updated = productMapper.toEntity(dto);
        return productMapper.toResponse(productRepo.save(updated));
    }

    @Override
    public List<CardProduct> getAllProducts() {
        return productRepo.findAll();
    }

    @Transactional
    @Override
    public FeeConfigResponse addFeeToProduct(Long productId, String type, Double amount) {
        CardProduct product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Card product not found"));

        FeeType feeType;
        try {
            feeType = FeeType.valueOf(type.toUpperCase().trim());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid fee type: " + type + ". Allowed: " + Arrays.toString(FeeType.values()));
        }

        BigDecimal amountBD = (amount == null) ? BigDecimal.ZERO : BigDecimal.valueOf(amount);

        FeeConfig fee = new FeeConfig(
                null,        // DB generates
                product,     // association
                feeType,     // enum
                amountBD     // BigDecimal
        );

        FeeConfig saved = feeRepo.save(fee);
        return feeMapper.toResponse(saved);
    }
}