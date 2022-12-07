package com.spring.quoctrong.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.Producer;
import com.spring.quoctrong.models.Product;

@Repository
public interface ProducerRepository  extends JpaRepository<Producer, Integer>{
	List<Producer> findAllByOrderByNameAsc();
	Optional<Producer> findByName(String name);
	List<Producer> findAllByOrderByNameDesc();
	Optional<Producer> findById(int id);
	void deleteById(int id);
	boolean existsByName(String name);
}
