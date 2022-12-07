package com.spring.quoctrong.services;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.dto.Statistic;
import com.spring.quoctrong.models.Invoice;
import com.spring.quoctrong.models.Order;
import com.spring.quoctrong.models.Receipt;
import com.spring.quoctrong.repositories.InvoiceRepository;
import com.spring.quoctrong.repositories.OrderRepository;
import com.spring.quoctrong.repositories.ReceiptRepository;

@Service
public class StatisticService {

	@Autowired
	InvoiceRepository invoiceRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	ReceiptRepository receiptRepository;

	
	
	public Statistic statisticAll() {
		List<Invoice> invoices = invoiceRepository.findAll();
		List<Receipt> receipts = receiptRepository.findAll();
		Statistic statistic = new Statistic();
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		float income = 0;
		float cost = 0;
		int soldProduct = 0;
		//Tổng thu
		for (Invoice invoice : invoices) {
			//San pham da ban
			soldProduct+=invoice.getOrder().getTotalQuantity();
			income += invoice.getOrder().getTotalPrice();	
		}
		//Tổng chi
		for (Receipt receipt : receipts) {
			cost += receipt.getTotalPrice();
		}
		statistic.setCost(vndFormat.format(cost));
		statistic.setSoldProduct(soldProduct);
		statistic.setIncome(vndFormat.format(income));
		statistic.setProfit(vndFormat.format(income - cost));
		
		
		
		return statistic;
		
	}
}
