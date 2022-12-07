package com.spring.quoctrong.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.Product;
import com.spring.quoctrong.models.Promotion;
@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer>{
	List<Promotion> findAllByOrderByNameAsc();
}
