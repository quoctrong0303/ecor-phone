package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.quoctrong.models.Product;
import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.repositories.ProductOptionRepository;
import com.spring.quoctrong.repositories.ProductRepository;

@Service
public class ProductOptionServiceImpl implements ProductOptionService {

	@Autowired
	ProductOptionRepository productOptionRepository;

	@Override
	public Optional<ProductOption> findById(int id) {
		return productOptionRepository.findById(id);
	}

	@Override
	public ProductOption convertToModel(String productOption) {
		ProductOption productModel = new ProductOption();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			productModel = objectMapper.readValue(productOption, ProductOption.class);
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
	public List<ProductOption> findAllByOrderByProductAsc() {
		return productOptionRepository.findAllByOrderByProductAsc();
	}

	@Override
	public List<ProductOption> findAllByOrderByProductDesc() {
		return productOptionRepository.findAllByOrderByProductDesc();
	}

	@Override
	public ProductOption save(ProductOption productOption) {
		return productOptionRepository.save(productOption);
	}

	@Override
	public void delete(ProductOption productOption) {
		productOptionRepository.delete(productOption);

	}

	@Override
	public List<ProductOption> findAllByRomAndRam(String rom, String ram) {
		return productOptionRepository.findAllByRomAndRam(rom, ram);
	}

}
