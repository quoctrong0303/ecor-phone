package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.quoctrong.models.Store;
import com.spring.quoctrong.models.Supplier;
import com.spring.quoctrong.repositories.StoreRepository;

@Service
public class StoreService {
	@Autowired
	StoreRepository storeRepository;

	public List<Store> findAllByOrderByNameAsc(){
		return storeRepository.findAllByOrderByNameAsc();
	};
	public List<Store> findAllByOrderByNameDesc(){
		return storeRepository.findAllByOrderByNameDesc();
	};
	public Optional<Store> findById(int id){
		return storeRepository.findById(id);
	};
	
	public boolean existById(int id){
		return storeRepository.existsById(id);
	};
	
	public Store convertToModel(String store) {
		Store storeModel = new Store();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			storeModel = objectMapper.readValue(store, Store.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return storeModel;
	}
}
