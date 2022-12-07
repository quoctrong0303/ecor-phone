package com.spring.quoctrong.models;

import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private float price;
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name = "orderId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "productOptionId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
	private ProductOption productOption;
	
	
	public String getFormatedPrice() {
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		return vndFormat.format(this.price);
	}
	
	public String getFormatedTotalPrice() {
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		return vndFormat.format(this.price * this.quantity);
	}
}
