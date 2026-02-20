package com.CardMaster.service.cpl;

import com.CardMaster.model.cpl.FeeConfig;
import com.CardMaster.dao.cpl.FeeConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeeConfigServiceImpl implements FeeConfigService {

    private final FeeConfigRepository repository;

    @Override
    public FeeConfig create(FeeConfig entity) {
        // add validation/business rules if needed
        return repository.save(entity);
    }

    @Override
    public List<FeeConfig> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<FeeConfig> findById(Long id) {
        return repository.findById(id);
    }
}