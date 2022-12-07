package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.quoctrong.models.ResponseObject;
import com.spring.quoctrong.models.Supplier;
import com.spring.quoctrong.repositories.SupplierRepository;

@Service
public class SupplierService {
	@Autowired
	SupplierRepository supplierRepository;
	
	public List<Supplier> findAllByOrderByNameAsc(){
		return supplierRepository.findAllByOrderByNameAsc();
	};
	public List<Supplier> findAllByOrderByNameDesc(){
		return supplierRepository.findAllByOrderByNameDesc();
	};
	public Optional<Supplier> findById(int id){
		return supplierRepository.findById(id);
	};
	
	public boolean existById(int id){
		return supplierRepository.existsById(id);
	};
	
	public ResponseObject add(Supplier supplier) {
		if (supplierRepository.existsByName(supplier.getName())) {
			return new ResponseObject("error", "Tên nhà cung cấp đã tồn tại!", "");
		}
		supplierRepository.save(supplier);
		return new ResponseObject("success", "Thêm nhà cung cấp thành công!", supplier);
	}
	
	public ResponseObject update(Supplier supplier) {
		Supplier supplier3;
		for (Supplier supplier2: supplierRepository.findAll()) {
			if (supplier.getName().equals(supplier2.getName()) && supplier.getId() != supplier2.getId()) {
				return new ResponseObject("error", "Nhà sản xuất đã tồn tại!", supplier);
			}
		}
		Optional<Supplier> updatedProducer = supplierRepository.findById(supplier.getId());
		if(updatedProducer.isPresent()) {
			supplier3 = updatedProducer.get();
			supplier3.setName(supplier.getName());
			supplier3.setAddress(supplier.getAddress());
			supplier3.setNumberphone(supplier.getNumberphone());
			supplierRepository.save(supplier3);
			
		} else {
			System.out.println(supplier.getId());
			supplierRepository.save(supplier);
			return new ResponseObject("success", "Thêm nhà cung cấp thành công!", supplier);
		}
				
				
		return new ResponseObject("success", "Cập nhật nhà cung cấp thành công!", supplier3);
	}
	
	public ResponseObject deleteById(int id){
		
		Optional<Supplier> supplierOpt = supplierRepository.findById(id);
		if (supplierOpt.isPresent()) {
			Supplier supplier = supplierOpt.get();
			supplierRepository.delete(supplier);
		} else {
			return new ResponseObject("error", "Không tìm thấy nhà cung cấp!", id);
		}

		return new ResponseObject("success", "Xoá nhà cung cấp thành công!", "");
	}
	
	public Supplier convertToModel(String supplier) {
		Supplier supplierModel = new Supplier();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			supplierModel = objectMapper.readValue(supplier, Supplier.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return supplierModel;
	}
}
