package com.CardMaster.service.paa;

import com.CardMaster.dao.cpl.CardProductRepository;
import com.CardMaster.dao.paa.CardApplicationRepository;
import com.CardMaster.dao.paa.CustomerRepository;
import com.CardMaster.dto.paa.CardApplicationDto;
import com.CardMaster.exception.paa.ApplicationNotFoundException;
import com.CardMaster.exception.paa.CustomerNotFoundException;
import com.CardMaster.exceptions.cpl.NotFoundException;
import com.CardMaster.mapper.paa.EntityMapper;
import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.model.paa.Customer;
import com.CardMaster.security.iam.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CardApplicationService {

    private final CardApplicationRepository applicationRepository;
    private final CustomerRepository customerRepository;
    private final CardProductRepository productRepo;
    private final JwtUtil jwtUtil;

    // --- Create Application (initially Submitted) ---
    public CardApplicationDto create(CardApplicationDto dto, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + dto.getCustomerId()));

        CardProduct product = productRepo.findById(dto.getProductId()) .orElseThrow(() -> new NotFoundException("Product not found"));
        CardApplication application = EntityMapper.toCardApplicationEntity(dto, customer,product);
        application.setStatus(CardApplication.CardApplicationStatus.Submitted);

        CardApplication saved = applicationRepository.save(application);
        return EntityMapper.toCardApplicationDto(saved);
    }

    // --- Get Application by ID ---
    public CardApplicationDto findById(Long id, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        CardApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found with id: " + id));
        return EntityMapper.toCardApplicationDto(app);
    }

    // --- Get All Applications ---
    public List<CardApplicationDto> getAllApplications(String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        List<CardApplication> apps = applicationRepository.findAll();
        List<CardApplicationDto> dtos = new ArrayList<>();
        for (CardApplication app : apps) {
            dtos.add(EntityMapper.toCardApplicationDto(app));
        }
        return dtos;
    }

    // --- Delete Application ---
    public void deleteApplication(Long id, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        if (!applicationRepository.existsById(id)) {
            throw new ApplicationNotFoundException("Application not found with id: " + id);
        }
        applicationRepository.deleteById(id);
    }
}
