package com.spring.quoctrong.controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.quoctrong.models.CartItem;
import com.spring.quoctrong.models.CustomUserDetails;
import com.spring.quoctrong.models.Order;
import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.models.ResponseObject;
import com.spring.quoctrong.services.CartService;
import com.spring.quoctrong.services.OrderService;
import com.spring.quoctrong.services.ProductOptionServiceImpl;

@Controller
public class CartController {
	@Autowired
	ProductOptionServiceImpl productOptionService;
	@Autowired
	CartService cartService;

	@GetMapping({"/cart", "/cart/"})
	public String listCart(Model model) {
		model.addAttribute("title", "Giỏ hàng của bạn");
		Collection<CartItem> cartItems = cartService.getCartitems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", cartService.getFormatedTotal());
		model.addAttribute("count", cartService.getCount());
		return "user/cart";
	}
	
	@PostMapping({"/cart/{id}", "/cart/{id}/"})
	@ResponseBody
	public ResponseEntity<ResponseObject> addCart(@PathVariable(value = "id") Integer id) {
		try {
			ProductOption productOption = productOptionService.findById(id).orElseThrow(()-> new Exception("Không tìm thấy sản phẩm!"));
			CartItem item = new CartItem(productOption, 1);
			cartService.add(item);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Thêm sản phẩm vào giỏ hàng thành công!", cartService.getCartItem(productOption)));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject("error", e.getMessage(), ""));
			
		}
		
	}
	
	@PutMapping({"/cart/{id}", "/cart/{id}/"})
	public ResponseEntity<ResponseObject> updateCart(@PathVariable(value = "id") Integer id, @RequestParam(value = "quantity") Integer quantity, Model mdel) {
		try {
			ProductOption productOption = productOptionService.findById(id).orElseThrow(() -> new Exception("Không tìm thấy sản phẩm!"));
			cartService.update(productOption, quantity);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Cập nhật giỏ hàng thành công!", cartService.getCartItem(productOption)));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject("error", e.getMessage(), ""));
		}
	}

	@DeleteMapping({"/cart/{id}", "/cart/{id}/"})
	public ResponseEntity<ResponseObject> removeCart(@PathVariable(value = "id") Integer id) {
		try {
			ProductOption productOption = productOptionService.findById(id).orElseThrow(() -> new Exception("Không tìm thấy sản phẩm!"));
			cartService.remove(productOption);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Xoá sản phẩm khỏi giỏ hàng thành công!", cartService.getFormatedTotal()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject("error", e.getMessage(), ""));
		}
	}
	
	
	//Checkout
	@PreAuthorize("isAuthenticated()")
	@GetMapping({"/checkout", "/checkout/"})
	public String checkout() {
		
		
		
		return "user/checkout";
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping({"/checkout", "/checkout/"})
	public String checkout(RedirectAttributes redirectAttributes) throws Exception{
		try {
			cartService.checkout();
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/checkout";
		}
		
		redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
		return "redirect:/shop";
	}
	
	
	//Order list
	@Autowired
	OrderService orderService;
	@PreAuthorize("isAuthenticated()")
	@GetMapping({"/order", "/order/"})
	public String order(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user =(CustomUserDetails) authentication.getPrincipal();
		List<Order> orders = orderService.findByUser(user.getUser());
		model.addAttribute("orders", orders);
		model.addAttribute("title", "Đơn hàng");
		return "user/order-list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping({"/order/{id}", "/order/{id}/"})
	public String orderDetail(Model model, @PathVariable(value = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails user =(CustomUserDetails) authentication.getPrincipal();
			Order order = orderService.findByOrderIdAndUser(id, user.getUser()).orElseThrow(() -> new Exception(("Không tìm thấy đơn đặt!")));
			model.addAttribute("order", order);
			model.addAttribute("title", "Chi tiết đơn hàng");
			return "user/order-detail";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errror", e.getMessage());
			return "redirect:/order";
		}
	}
	
	
	
	
}
