package com.CardMaster.dao.cpl;

import com.CardMaster.model.cpl.CardProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardProductRepository extends JpaRepository<CardProduct, Long> {
   //boolean existsByName(String name);
    List<CardProduct> findAll();
}
