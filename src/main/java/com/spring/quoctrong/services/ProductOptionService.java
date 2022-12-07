package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.Product;
import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.repositories.ProductOptionRepository;

public interface ProductOptionService {
	
	List<ProductOption> findAllByOrderByProductAsc();
	List<ProductOption> findAllByOrderByProductDesc();
	List<ProductOption> findAllByRomAndRam(String rom, String ram);
	public Optional<ProductOption> findById(int id) ;
	public ProductOption convertToModel(String product);
	public ProductOption save(ProductOption productOption);
	public void delete(ProductOption productOption);
	;
}
