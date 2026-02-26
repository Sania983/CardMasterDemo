package com.CardMaster.service.tap;

import com.CardMaster.dao.tap.TransactionRepository;
import com.CardMaster.dao.tap.TransactionHoldRepository;
import com.CardMaster.mapper.tap.TransactionMapper;
import com.CardMaster.model.tap.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionHoldRepository holdRepository;
    private final TransactionMapper transactionMapper;

    // ... your other methods (authorize, post, fail, reverse, getById) ...

    @Transactional(readOnly = true)
    public List<Transaction> listAll() {
        return transactionRepository.findAll();
    }
}