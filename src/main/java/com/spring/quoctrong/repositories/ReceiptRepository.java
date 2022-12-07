package com.spring.quoctrong.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.Receipt;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Integer>{

	List<Receipt> findAllByOrderByStoreAsc();
	List<Receipt> findAllByOrderByStoreDesc();
	Optional<Receipt> findById(int id);
	void deleteById(int id);
	boolean existsById(int id); 
}
