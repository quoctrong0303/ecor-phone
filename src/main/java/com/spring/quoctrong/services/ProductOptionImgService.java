package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.Product;
import com.spring.quoctrong.models.ProductImage;
import com.spring.quoctrong.repositories.productOptionImgRepository;

@Service
public class ProductOptionImgService {

	@Autowired
	productOptionImgRepository productOptionImgRepository;
	
	public ProductImage save (ProductImage productImage) {
		return productOptionImgRepository.save(productImage);
	}
	
	public Optional<ProductImage> findById(int id) {
		return productOptionImgRepository.findById(id);
	}
	
	public void deleteById(int id) {
		productOptionImgRepository.deleteById(id);
	}
	
	public boolean existsById(int id) {
		return productOptionImgRepository.existsById(id);
	}
	
}
