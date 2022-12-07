package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.Order;
import com.spring.quoctrong.models.State;
import com.spring.quoctrong.models.User;
import com.spring.quoctrong.repositories.OrderRepository;
import com.spring.quoctrong.repositories.StateRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	StateRepository stateRepository;
	
	public List<Order> findAll() {
		return orderRepository.findAll();
	}
	
	public Optional<Order> findById(Integer id) {
		return orderRepository.findById(id);
	}
	
	public List<Order> findByUser(User user) {
		return orderRepository.findByUser(user);
	}
	
	public Optional<Order> findByOrderIdAndUser(Integer id, User user) {
		return orderRepository.findByIdAndUser(id, user);
	}
	
	public Order changeState(Integer orderId, Integer stateId) throws Exception{
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new Exception("Đơn hàng không tồn tại!"));
		State state = stateRepository.findById(stateId).orElseThrow(() -> new Exception("Trạng thái đơn không hợp lệ!"));
		
		if (order.getState().getName().equals("Giao thành công")) {
			throw new Exception("Lỗi: Đơn hàng đã được giao!");
		}
		order.setState(state);
		return orderRepository.save(order);
		
	}
}
