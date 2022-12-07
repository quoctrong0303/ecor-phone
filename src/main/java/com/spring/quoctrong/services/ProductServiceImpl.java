package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.quoctrong.models.Producer;
import com.spring.quoctrong.models.Product;
import com.spring.quoctrong.repositories.ProductRepository;
@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductRepository productRepository;
	
	@Override
	public Optional<Product> findProductById(int id) {
		return productRepository.findById(id);
	}
	@Override
	public boolean existsByName(String name) {
		return productRepository.existsByName(name);
	}
	@Override
	public Optional<Product> findProductByName(String name) {
		return productRepository.findByName(name);
	}
	@Override
	public Product convertToModel(String product) {
		Product productModel = new Product();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			productModel = objectMapper.readValue(product, Product.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return productModel;
	}
	
	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}
	@Override
	public List<Product> findAllByOrderByNameAsc() {
		return productRepository.findAllByOrderByNameAsc();
	}
	@Override
	public List<Product> findAllByOrderByNameDesc() {
		return productRepository.findAllByOrderByNameDesc();
	}
	@Override
	public List<Product> findAllByProducer(Producer producer) {
		return productRepository.findAllByProducer(producer);
	}
	@Override
	public List<Product> findTop5ByNameContaining(String name) {
		return productRepository.findTop5ByNameContaining(name);
	}
	@Override
	public List<Product> findByNameContaining(String name) {
		return productRepository.findTop5ByNameContaining(name);
	}
	
}
