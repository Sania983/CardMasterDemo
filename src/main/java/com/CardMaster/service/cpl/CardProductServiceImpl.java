package com.CardMaster.service.cpl;

import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.dao.cpl.CardProductRepository; // <-- adjust package to your actual repo package
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardProductServiceImpl implements CardProductService {

    private final CardProductRepository repository;

    @Override
    public CardProduct create(CardProduct entity) {
        // add validation/business rules if needed
        return repository.save(entity);
    }

    @Override
    public List<CardProduct> findAll() {
        return repository.findAll();
    }
}