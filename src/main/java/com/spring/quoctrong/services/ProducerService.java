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
import com.spring.quoctrong.models.ResponseObject;
import com.spring.quoctrong.repositories.ProducerRepository;

@Service
public class ProducerService {

	@Autowired
	ProducerRepository producerRepository;
	
	public List<Producer> findAllByOrderByNameAsc(){
		return producerRepository.findAllByOrderByNameAsc();
	};
	public List<Producer> findAllByOrderByNameDesc(){
		return producerRepository.findAllByOrderByNameDesc();
	};
	public Optional<Producer> findById(int id){
		return producerRepository.findById(id);
	};
	
	public Optional<Producer> findByName(String name){
		return producerRepository.findByName(name);
	};
	
	public boolean existById(int id){
		return producerRepository.existsById(id);
	};
	
	public ResponseObject add(Producer producer) {
		if (producerRepository.existsByName(producer.getName())) {
			return new ResponseObject("error", "Tên nhà sản xuất đã tồn tại!", "");
		}
		producerRepository.save(producer);
		return new ResponseObject("success", "Thêm nhà sản xuất thành công!", producer);
	}
	
	public ResponseObject update(Producer producer) {
		Producer producer3;
		for (Producer producer2: producerRepository.findAll()) {
			if (producer.getName().equals(producer2.getName()) && producer.getId() != producer2.getId()) {
				return new ResponseObject("error", "Nhà sản xuất đã tồn tại!", producer);
			}
		}
		Optional<Producer> updatedProducer = producerRepository.findById(producer.getId());
		if(updatedProducer.isPresent()) {
			producer3 = updatedProducer.get();
			producer3.setName(producer.getName());
			producerRepository.save(producer3);
			
		} else {
			System.out.println(producer.getId());
			producerRepository.save(producer);
			return new ResponseObject("success", "Thêm nhà sản xuất thành công!", producer);
		}
				
				
		return new ResponseObject("success", "Cập nhật nhà sản xuất thành công!", producer3);
	}
	
	public ResponseObject deleteById(int id){
		
		Optional<Producer> producerOpt = producerRepository.findById(id);
		if (producerOpt.isPresent()) {
			Producer producer = producerOpt.get();
			producerRepository.delete(producer);
		} else {
			return new ResponseObject("error", "Không tìm thấy nhà sản xuất!", id);
		}

		return new ResponseObject("success", "Xoá nhà sản xuất thành công!", "");
	}
	
	public Producer convertToModel(String producer) {
		Producer producerModel = new Producer();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			producerModel = objectMapper.readValue(producer, Producer.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return producerModel;
	}
}
