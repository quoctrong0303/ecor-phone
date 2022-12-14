package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.models.Receipt;
import com.spring.quoctrong.models.ReceiptDetail;
import com.spring.quoctrong.models.ReceiptItemDTO;
import com.spring.quoctrong.models.ResponseObject;
import com.spring.quoctrong.models.Stock;
import com.spring.quoctrong.repositories.ProductOptionRepository;
import com.spring.quoctrong.repositories.ReceiptDetailRepository;
import com.spring.quoctrong.repositories.StockRepository;
@Service
public class ReceiptDetailService {

	@Autowired
	ReceiptDetailRepository receiptDetailRepository;
	@Autowired
	ProductOptionRepository productOptionRepository;
	@Autowired
	StockRepository stockRepository;
	
	public ReceiptDetail save(ReceiptDetail receiptDetail) {
		return receiptDetailRepository.save(receiptDetail);
	}
	
	public List<ReceiptDetail> findByReceiptAndProductOption(Receipt receipt, ProductOption productOption) {
		return receiptDetailRepository.findByReceiptAndProductOption(receipt, productOption);
	}
	
	public ResponseObject add (Receipt receipt, ReceiptItemDTO receiptItemDTO, boolean isAddToStock) {
		
		Optional<ProductOption> productOptionOpt = productOptionRepository.findById(receiptItemDTO.getProductOptionId());
		ReceiptDetail receiptDetail = new ReceiptDetail();
		if (productOptionOpt.isPresent() && receiptItemDTO.getQuantity() > 0) {
			ProductOption productOption = productOptionOpt.get();
			List<ReceiptDetail> lReceiptDetails = receiptDetailRepository.findByReceiptAndProductOption(receipt, productOption);
			if (lReceiptDetails.size() > 0) {
				receiptDetail = lReceiptDetails.get(0);
				int oldQuantity = receiptDetail.getQuantity();
				receiptDetail.setQuantity(oldQuantity + receiptItemDTO.getQuantity());
				receiptDetailRepository.save(receiptDetail);
			} else {
				receiptDetail = new ReceiptDetail(receiptItemDTO.getQuantity(), receiptItemDTO.getPrice(), productOption, receipt);
				receiptDetailRepository.save(receiptDetail);
			}
			//N???u ch???n th??m s???n ph???m v??o kho
			if (isAddToStock) {
				Stock stock;
				List<Stock> lStocks = stockRepository.findByStoreAndProductOption(receipt.getStore(), productOption);
				//N???u ???? t???n t???i s???n ph???m trong kho th?? t??ng s??? l?????ng l??m
				if (lStocks.size() > 0) {
					stock = lStocks.get(0);
					int oldQuantity = stock.getQuantity();
					stock.setQuantity(oldQuantity + receiptItemDTO.getQuantity());
					stockRepository.save(stock);
				} else {//Kh??ng th?? t???o m???i s???n ph???m trong kho
					stock = new Stock(receiptItemDTO.getQuantity(), productOption, receipt.getStore());
					stockRepository.save(stock);
				}
				
			}
			
		}
		
		return new ResponseObject("success", "Th??m th??nh c??ng!", receiptDetail);
	}
}
