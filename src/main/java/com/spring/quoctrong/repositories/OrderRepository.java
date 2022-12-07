package com.spring.quoctrong.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.Order;
import com.spring.quoctrong.models.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	List<Order> findByUser(User user);
	Optional<Order> findByIdAndUser(Integer id, User user);
}
