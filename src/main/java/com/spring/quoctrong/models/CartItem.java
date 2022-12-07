package com.spring.quoctrong.models;

import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.quoctrong.services.CartService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

	private ProductOption productOption;
	private Integer quantity;
	
	
	public double getPrice() {
		return this.getProductOption().getPriceHasDiscount() * quantity;
	}
	
	//trường này không có trong database
	//dùng để lưu tổng số tiền của cả giỏ hàng
	//khi update giỏ hàng, gọi service tính tổng giỏ hàng và set trường này vào.
	@Transient
	private String formatedTotalPrice;
	
	@Transient
	public String getFormatedPrice() {
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		return vndFormat.format(this.getQuantity() * this.getProductOption().getSellingPrice());
	}
	
	@Transient
	public String getFormatedPriceHasDiscount() {
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		return vndFormat.format(this.getPrice());
	}

	public CartItem(ProductOption productOption, Integer quantity) {
		super();
		this.productOption = productOption;
		this.quantity = quantity;
	}
	
	
	
}
