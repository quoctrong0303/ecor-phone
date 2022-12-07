package com.spring.quoctrong.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.Producer;
import com.spring.quoctrong.models.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	List<Product> findAllByOrderByNameAsc();
	List<Product> findAllByOrderByNameDesc();
	List<Product> findAllByProducer(Producer producer);
	Optional<Product> findByName(String name);
	boolean existsByName(String name);
	//tên chứa (contain) "name"
	List<Product> findTop5ByNameContaining(String name);
	List<Product> findByNameContaining(String name);
	
}
