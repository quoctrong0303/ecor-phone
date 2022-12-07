package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.models.Receipt;
import com.spring.quoctrong.models.ResponseObject;
import com.spring.quoctrong.models.Store;
import com.spring.quoctrong.models.Supplier;
import com.spring.quoctrong.repositories.ProductOptionRepository;
import com.spring.quoctrong.repositories.ReceiptRepository;
import com.spring.quoctrong.repositories.StoreRepository;
import com.spring.quoctrong.repositories.SupplierRepository;

@Service
public class ReceiptService {

	@Autowired
	ReceiptRepository receiptRepository;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	ProductOptionRepository productOptionRepository;
	@Autowired
	SupplierRepository supplierRepository;
	
	
	public List<Receipt> findAllByOrderByStoreAsc(){
		return receiptRepository.findAllByOrderByStoreAsc();
	};
	public List<Receipt> findAllByOrderByStoreDesc(){
		return receiptRepository.findAllByOrderByStoreDesc();
	};
	public Optional<Receipt> findById(int id){
		return receiptRepository.findById(id);
	};
	
	public boolean existById(int id){
		return receiptRepository.existsById(id);
	};
	public Receipt save(Receipt receipt) {
		return receiptRepository.save(receipt);
	}
	
	
	
	//thêm receipt mới
	public Receipt add(int storeId, int supplierId) throws Exception{
		
		Optional<Store> storeOpt = storeRepository.findById(storeId);
		Optional<Supplier> supplierOpt = supplierRepository.findById(supplierId);
		
		Store store = new Store();
		Supplier supplier = new Supplier();
		
		if (storeOpt.isPresent()) {
			store = storeOpt.get();

		} else {
			throw new Exception("Không tìm thấy cửa hàng!");
		}
		if (supplierOpt.isPresent()) {
			supplier = supplierOpt.get();

		} else {
			throw new Exception("Không tìm thấy nhà cung cấp!");
		}
		
		Receipt receipt = new Receipt(store, supplier);
		receipt = receiptRepository.save(receipt);

		
		
		
		return receipt;
	}
	
	public ResponseObject deleteById(int id) throws Exception{
		Optional<Receipt> receiptOpt = receiptRepository.findById(id);
		if (receiptOpt.isPresent()) {
			Receipt receipt = receiptOpt.get();
			receiptRepository.delete(receipt);
		} else {
			throw new Exception("Không tìm thấy đơn nhập hàng!");
		}
		
		
		return new ResponseObject("success", "Xoá đơn nhập hàng thành công!", "");
	}
	

}
