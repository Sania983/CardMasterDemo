package com.CardMaster.service.cpl;

import com.CardMaster.dto.cpl.CardProductDto;
import com.CardMaster.model.cpl.CardProduct;
import com.CardMaster.model.iam.User;
import com.CardMaster.Enum.cpl.ProductStatus;
import com.CardMaster.Enum.iam.Role;
import com.CardMaster.exception.cpl.BadRequestException;
import com.CardMaster.exception.cpl.EntityNotFoundException;
import com.CardMaster.exception.UnauthorizedActionException;
import com.CardMaster.mapper.cpl.CardProductMapper;
import com.CardMaster.dao.cpl.CardProductRepository;
import com.CardMaster.dao.iam.UserRepository1;
import com.CardMaster.security.iam.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardProductService {

    private final CardProductRepository productRepository;
    private final CardProductMapper productMapper;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /**
     * Create a new card product (Admin only).
     */
    public CardProductDto create(CardProductDto dto, String bearerToken) {
        String username = jwtUtil.extractUsername(stripBearer(bearerToken));
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User", null));

        if (user.getRole() != Role.ADMIN) {
            throw new UnauthorizedActionException("Only admins can create card products");
        }

        // Optional validations
        if (dto.getAnnualFee() != null && dto.getAnnualFee() < 0) {
            throw new BadRequestException("Annual fee must be positive");
        }
        if (dto.getInterestRate() != null && dto.getInterestRate() < 0) {
            throw new BadRequestException("Interest rate must be positive");
        }

        CardProduct product = productMapper.toEntity(dto);
        product.setStatus(ProductStatus.ACTIVE);

        CardProduct saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    /**
     * List all products (any authenticated user).
     */
    public List<CardProductDto> findAll(String bearerToken) {
        jwtUtil.extractUsername(stripBearer(bearerToken)); // validate token existence & signature
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    /**
     * Get a product by ID (any authenticated user).
     */
    public CardProductDto findById(Long id, String bearerToken) {
        jwtUtil.extractUsername(stripBearer(bearerToken));
        CardProduct product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CardProduct", id));
        return productMapper.toDto(product);
    }

    /**
     * Deactivate product (Admin only).
     */
    public CardProductDto deactivate(Long id, String bearerToken) {
        String username = jwtUtil.extractUsername(stripBearer(bearerToken));
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User", null));

        if (user.getRole() != Role.ADMIN) {
            throw new UnauthorizedActionException("Only admins can deactivate products");
        }

        CardProduct product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CardProduct", id));

        // If already inactive, you may either return as-is or throw BadRequest/Conflict
        if (product.getStatus() == ProductStatus.INACTIVE) {
            // throw new BadRequestException("Product is already inactive");
        }

        product.setStatus(ProductStatus.INACTIVE);
        CardProduct saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    private String stripBearer(String token) {
        // expects "Bearer <jwt>"
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : token;
    }
}
