package com.spring.quoctrong.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.quoctrong.models.Producer;
import com.spring.quoctrong.models.Product;
import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.models.ResponseObject;
import com.spring.quoctrong.models.Stock;
import com.spring.quoctrong.models.Store;
import com.spring.quoctrong.repositories.StockRepository;
import com.spring.quoctrong.services.ProducerService;
import com.spring.quoctrong.services.ProductOptionServiceImpl;
import com.spring.quoctrong.services.ProductServiceImpl;
import com.spring.quoctrong.services.StockService;
import com.spring.quoctrong.services.StoreService;

@Controller
public class ShopController {
	
	@Autowired
	ProductServiceImpl productService;
	@Autowired
	ProducerService producerService;
	@Autowired
	ProductOptionServiceImpl productOptionService;

	@GetMapping({"/shop", "shop/"})
	public String shop(Model model, @RequestParam(required = false, value = "q") String name) {
		if (name != null) {
			List<Product> products = productService.findByNameContaining(name);
			model.addAttribute("searchvalue", name);
			model.addAttribute("products", products);
		} else {
			List<Product> products = productService.findAllByOrderByNameAsc();
			model.addAttribute("products", products);
		}
		model.addAttribute("title", "Sản phẩm");	
		List<Producer> producers = producerService.findAllByOrderByNameAsc();
		model.addAttribute("producers", producers);
		return "user/shop";
	}
	
	@GetMapping({"/api/v1/shop", "api/v1/shop/"})
	@ResponseBody
	public ResponseEntity<?> shop2(Model model, @RequestParam(required = false, value = "q") String name) {
		List<Product> products= new ArrayList<>();
		if (name != null) {
			products = productService.findTop5ByNameContaining(name);
			model.addAttribute("products", products);
		} else {
			return ResponseEntity.ok().body(new ResponseObject("error", "Không tìm thấy sản phẩm!", ""));
		}
		return ResponseEntity.ok().body(new ResponseObject("success", "Truy vấn thành công!", products));
	}
	
	
	
	
	@GetMapping({"/shop/filter", "shop/filter"})
	public String shopByProducer(Model model, @RequestParam(value = "producer") String name, RedirectAttributes redirectAttributes) {
		Optional<Producer> producer = producerService.findByName(name);
		if (producer.isPresent()) {
			List<Product> products = productService.findAllByProducer(producer.get());
			model.addAttribute("title", "Sản phẩm của " + producer.get().getName());
			List<Producer> producers = producerService.findAllByOrderByNameAsc();
			model.addAttribute("producers", producers);
			model.addAttribute("products", products);
			return "user/shop";
		} else {
			redirectAttributes.addFlashAttribute("error", "Nhà sản xuất không tồn tại!");
			return "redirect:/shop";
		}
		
	}
	
	@Autowired
	StockService stockService;
	@Autowired
	StoreService storeService;
	
	@GetMapping({"/product/{id}", "/product/{id}/"})
	public String productDetail(Model model, @RequestParam(value = "id") Integer productOptionId
			, @PathVariable(value = "id") Integer productId
			, RedirectAttributes redirectAttributes) {	
		try {
			Product product = productService.findProductById(productId).orElseThrow(() -> new Exception("Không tìm thấy sản phẩm!"));
			ProductOption productOption = productOptionService.findById(productOptionId).orElseThrow(() -> new Exception("Mã số không hợp lệ!"));
			Store store = storeService.findById(0).orElseThrow(() -> new Exception("Không tìm thấy cửa hàng!"));
			List<Stock> stock = stockService.findByStoreAndProductOption(store, productOption);
			if(stock.size() > 0) {
				model.addAttribute("stock", stock.get(0));
			}
			model.addAttribute("productOption", productOption);
			model.addAttribute("product", product);
			model.addAttribute("title", productOption.getDisplayName());
			return "user/product-detail";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/shop";
		}
	}
	
	
}
