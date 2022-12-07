package com.spring.quoctrong.services;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.annotation.SessionScope;

import com.spring.quoctrong.models.CartItem;
import com.spring.quoctrong.models.CustomUserDetails;
import com.spring.quoctrong.models.Order;
import com.spring.quoctrong.models.OrderDetail;
import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.models.State;
import com.spring.quoctrong.models.Stock;
import com.spring.quoctrong.models.Store;
import com.spring.quoctrong.repositories.OrderDetailRepository;
import com.spring.quoctrong.repositories.OrderRepository;
import com.spring.quoctrong.repositories.StateRepository;
import com.spring.quoctrong.repositories.StockRepository;
import com.spring.quoctrong.repositories.StoreRepository;

@Service
@SessionScope
public class CartService {
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderDetailRepository orderDetailRepository;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	StateRepository stateRepository;
	@Autowired
	StockRepository stockRepository;
	
	private Map<Integer, CartItem> map = new HashMap<Integer, CartItem>();
	
	public void add(CartItem item) throws Exception {
		Store store = storeRepository.findById(0).orElseThrow(() -> new Exception("Không tìm thấy cửa hàng!"));
		Stock stock = stockRepository.findByStoreAndProductOption(store, item.getProductOption()).get(0);
		if (stock.getQuantity() < item.getQuantity()) throw new Exception("Kho hàng không đủ số lượng!");
		CartItem  existedItem = map.get(item.getProductOption().getId());
		if (existedItem != null) {
			existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
		} else {
			map.put(item.getProductOption().getId(), item);
		}
		
	}
	
	public void remove(ProductOption productOption) {
		map.remove(productOption.getId());
	}
	
	public Collection<CartItem> getCartitems() {
		return map.values();
	}
	
	public CartItem getCartItem(ProductOption productOption) {
		return map.get(productOption.getId());
	}
	
	
	public void clear() {
		map.clear();
	}
	
	public void update(ProductOption productOption, Integer quantity) throws Exception {
		CartItem item = map.get(productOption.getId());
		if (quantity <= 0) {
			throw new Exception("Số lượng không được nhỏ hơn 1");
		}
		Store store = storeRepository.findById(0).orElseThrow(() -> new Exception("Không tìm thấy cửa hàng!"));
		Stock stock = stockRepository.findByStoreAndProductOption(store, productOption).get(0);
		if (stock.getQuantity() < quantity) throw new Exception("Kho hàng không đủ số lượng!");
		item.setQuantity(quantity);
		item.setFormatedTotalPrice(this.getFormatedTotal());
	}
	
	public double getTotal() {
		return map.values().stream().mapToDouble(item -> item.getQuantity() * item.getProductOption().getPriceHasDiscount()).sum();
	}
	
	public Integer getQuantity() {
		return map.values().stream().mapToInt(item -> item.getQuantity()).sum();
	}

	public String getFormatedTotal () {
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		return vndFormat.format(this.getTotal());
	}
	public Integer getCount() {
		if (map.isEmpty()) return 0;
		return map.values().size();
	}
	
	public void checkout() throws Exception{
		try {
			//Xử lý khi giỏ hàng trống
			if (this.getCount() <= 0) {
				throw new Exception("Lỗi: Giỏ hàng trống!");
			}
			Order order = new Order();
			//Gán trạng thái đơn đặt là đang chờ xác nhận
			State state = stateRepository.findById(0).orElseThrow(() -> new Exception("Không tìm thấy trạng thái đơn đặt!"));		
			order.setState(state);
			Store store = storeRepository.findById(0).orElseThrow(() -> new Exception("Không tìm thấy cửa hàng!"));
			order.setStore(store);
			order.setTotalPrice((float)this.getTotal());
			order.setTotalQuantity(this.getQuantity());
			order.setTotalCount(this.getCount());
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails user =(CustomUserDetails) authentication.getPrincipal();	
			order.setUser(user.getUser());
			orderRepository.save(order);
			Collection<CartItem> cartItems = this.getCartitems();
			for (CartItem cartItem : cartItems) {
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setOrder(order);
				orderDetail.setQuantity(cartItem.getQuantity());
				orderDetail.setProductOption(cartItem.getProductOption());
				//Ở đây chia cho quantity vì Price của cartItem là đã nhân với quantity rồi. 
				orderDetail.setPrice((float)cartItem.getPrice() / (float)cartItem.getQuantity());
				orderDetailRepository.save(orderDetail);
			}			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
