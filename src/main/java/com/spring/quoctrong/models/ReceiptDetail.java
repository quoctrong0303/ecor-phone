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
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "receipt_details")
public class ReceiptDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int quantity;
	
	private float price;
	
	@ManyToOne
	@JoinColumn(name = "productOptionId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
	private ProductOption productOption;
	
	@ManyToOne
	@JoinColumn(name = "receiptId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
	private Receipt receipt;

	public ReceiptDetail(int quantity, float price, ProductOption productOption, Receipt receipt) {
		super();
		this.price = price;
		this.quantity = quantity;
		this.productOption = productOption;
		this.receipt = receipt;
	}
	
	//Hàm tính tổng giá của một sản phẩm
	@Transient
	public float getTotalPrice() {
		return this.quantity * this.price;
	}
	
	//Hàm định dạng chuyển từ number sang currency
	@Transient
	public String getFormatedTotalPrice() {
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		return vndFormat.format(this.getTotalPrice());
	}
	
	@Transient
	public String getFormatedPrice() {
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		return vndFormat.format(this.price);
	}
	
	


	
	
}
