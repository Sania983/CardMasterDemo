package com.CardMaster.dao.cpl;

import com.CardMaster.Entity.CardProduct;
import com.CardMaster.Enum.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardProductRepository extends JpaRepository<CardProduct, Long> {

    List<CardProduct> findByStatus(ProductStatus status);
}
