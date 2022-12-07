package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.models.ResponseObject;
import com.spring.quoctrong.models.Stock;
import com.spring.quoctrong.models.Store;
import com.spring.quoctrong.repositories.ProductOptionRepository;
import com.spring.quoctrong.repositories.StockRepository;
import com.spring.quoctrong.repositories.StoreRepository;

@Service
public class StockService {

	@Autowired
	StockRepository stockRepository;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	ProductOptionRepository productOptionRepository;
	
	public Stock save(Stock stock) {
		return stockRepository.save(stock);
	}
	
	public List<Stock> findByStoreAndProductOption(Store store, ProductOption productOption) {
		return stockRepository.findByStoreAndProductOption(store, productOption);
	}
	
	public Stock add (int storeId, int productOptionId, int Quantity) throws Exception{
		Optional<Store> storeOpt = storeRepository.findById(storeId);
		Optional<ProductOption> productOptionOpt = productOptionRepository.findById(productOptionId);
		Store store = new Store();
		Stock stock = new Stock();
		ProductOption productOption = new ProductOption();
		if (storeOpt.isPresent()) {
			store = storeOpt.get();		
		} else {
			throw new Exception("Không tìm thấy cửa hàng!");
		}
		
		if (productOptionOpt.isPresent()) {
			productOption = productOptionOpt.get();		
		} else {
			throw new Exception("Không tìm thấy sản phẩm!");
		}
		
		List<Stock> lStocks = stockRepository.findByStoreAndProductOption(store, productOption);
		if (lStocks.size() > 0) {
			stock = lStocks.get(0);
			int oldQuantity = stock.getQuantity();
			stock.setQuantity(oldQuantity + Quantity);
			stockRepository.save(stock);
		} else {
			stock = new Stock(Quantity, productOption, store);
			stockRepository.save(stock);
		}
		
		
		return stock;
	}
}
