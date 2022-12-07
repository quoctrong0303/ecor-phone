package com.spring.quoctrong.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer>{

	List<Store> findAllByOrderByNameAsc();
	List<Store> findAllByOrderByNameDesc();
	Optional<Store> findById(int id);
	void deleteById(int id);
	boolean existsByName(String name);
}
