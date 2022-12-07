package com.spring.quoctrong.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.models.Receipt;
@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Integer>{
	
	List<ProductOption> findAllByOrderByProductAsc();
	List<ProductOption> findAllByOrderByProductDesc();
	Optional<ProductOption> findById(int id);
	List<ProductOption> findAllByRomAndRam(String rom, String ram);
	void deleteById(int id);
	boolean existsById(int id); 
}
