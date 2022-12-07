package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.Invoice;
import com.spring.quoctrong.models.Order;
import com.spring.quoctrong.models.OrderDetail;
import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.models.Stock;
import com.spring.quoctrong.repositories.InvoiceRepository;
import com.spring.quoctrong.repositories.StockRepository;

@Service
public class InvoiceService {
	@Autowired
	InvoiceRepository invoiceRepository;
	@Autowired
	StockRepository stockRepository;

	public List<Invoice> findAll() {
		return invoiceRepository.findAll();
	}
	
	public Optional<Invoice> findById(Integer id) {
		return invoiceRepository.findById(id);
	}
	
	public Invoice createInvoice(Order order)  throws Exception{
		Invoice invoice = new Invoice();
		invoice.setOrder(order);
		//Giảm số lượng sản phẩm trong kho
		for (OrderDetail orderDetail : order.getOrderDetails()){
			Stock stock = stockRepository.findByStoreAndProductOption(order.getStore(), orderDetail.getProductOption()).get(0);
			if (stock != null) {
				//Nếu sản phẩm trong kho nhỏ hơn số lượng sản phẩm trong đơn đặt
				if (stock.getQuantity() < orderDetail.getQuantity()) throw new Exception("Sản phẩm trong kho không đủ!");
				stock.setQuantity(stock.getQuantity() - orderDetail.getQuantity());
				stockRepository.save(stock);
			}
		}
		
		return invoiceRepository.save(invoice);
	}
}
