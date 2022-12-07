package com.spring.quoctrong.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.quoctrong.models.CartItem;
import com.spring.quoctrong.models.Producer;
import com.spring.quoctrong.models.Product;
import com.spring.quoctrong.services.CartService;
import com.spring.quoctrong.services.ProducerService;
import com.spring.quoctrong.services.ProductOptionServiceImpl;
import com.spring.quoctrong.services.ProductServiceImpl;

@Controller
@RequestMapping({"/", "/home", "/index"})
public class HomeController {
	
	@Autowired
	ProductServiceImpl productService;
	@Autowired
	ProductOptionServiceImpl productOptionService;
	@Autowired
	ProducerService producerService;
	@Autowired
	CartService cartService;

	@GetMapping
	public String index(Model model) {
		model.addAttribute("title", "Trang chủ");
		List<Product> products = productService.findAll();
		List<Producer> producers = producerService.findAllByOrderByNameAsc();
		model.addAttribute("products", products);
		model.addAttribute("producers", producers);
		return "user/index";
	}
	
}
