package com.spring.quoctrong.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.Supplier;
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer>{
	List<Supplier> findAllByOrderByNameAsc();
	List<Supplier> findAllByOrderByNameDesc();
	Optional<Supplier> findById(int id);
	void deleteById(int id);
	boolean existsByName(String name);
}
