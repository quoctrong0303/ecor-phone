package com.spring.quoctrong.models;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private float totalPrice;
	//Số lượng sản phẩm
	private int totalQuantity;
	//Số loại sản phẩm
	private int totalCount;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@OneToMany(mappedBy = "order")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    @JsonIgnore
	private  Set<OrderDetail> orderDetails = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name = "userId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    @JsonIgnore
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "stateId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
	private State state;
	
	@ManyToOne
	@JoinColumn(name = "storeId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    @JsonIgnore
	private Store store;
		
	
	@OneToOne(mappedBy = "order")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
	private Invoice invoice;
	
	public String getFormatedTotalPrice() {
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		return vndFormat.format(this.totalPrice);
	}
	
	@Transient
	public int getDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.createdDate);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	@Transient
	public int getMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.createdDate);
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	@Transient
	public int getYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.createdDate);
		return calendar.get(Calendar.YEAR);
	}

	
}
