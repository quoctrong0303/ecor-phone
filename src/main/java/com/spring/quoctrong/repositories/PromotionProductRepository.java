package com.spring.quoctrong.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.PromotionProduct;

@Repository
public interface PromotionProductRepository extends JpaRepository<PromotionProduct, Integer>{
	Optional<PromotionProduct> findByProductId(Integer id);
}
