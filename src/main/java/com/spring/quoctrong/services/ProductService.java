package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import com.spring.quoctrong.models.Producer;
import com.spring.quoctrong.models.Product;

public interface ProductService {
	
	public Optional<Product> findProductById(int id) ;
	public Optional<Product> findProductByName(String name);
	public Product convertToModel(String product);
	public boolean existsByName(String name);
	public List<Product> findAll();
	public List<Product> findAllByProducer(Producer producer);
	public List<Product> findAllByOrderByNameAsc();
	public List<Product> findAllByOrderByNameDesc();
	public List<Product> findTop5ByNameContaining(String name);
	public List<Product> findByNameContaining(String name);
}
