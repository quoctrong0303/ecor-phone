package com.spring.quoctrong.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.models.Stock;
import com.spring.quoctrong.models.Store;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer>{

	List<Stock> findByStoreAndProductOption(Store store, ProductOption productOption);
}
