package com.spring.quoctrong.models;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "receipts")
public class Receipt {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createdDate;

	@OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
	@ToString.Exclude // Khoonhg sử dụng trong toString()
	private Set<ReceiptDetail> receiptDetails = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "storeId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
	@ToString.Exclude // Khoonhg sử dụng trong toString()
	@JsonIgnore
	private Store store;

	@ManyToOne
	@JoinColumn(name = "supplierId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
	@ToString.Exclude // Khoonhg sử dụng trong toString()
	@JsonIgnore
	private Supplier supplier;

	public Receipt(Date createdDate, Set<ReceiptDetail> receiptDetails, Store store, Supplier supplier) {
		super();
		this.createdDate = createdDate;
		this.receiptDetails = receiptDetails;
		this.store = store;
		this.supplier = supplier;
	}

	public Receipt(Store store, Supplier supplier) {
		super();
		this.store = store;
		this.supplier = supplier;
	}

	//Hàm tính tổng của cả hoá đơn
	@Transient
	private float totalPrice;

	public float getTotalPrice() {
		float total = 0;
		for (ReceiptDetail item : this.getReceiptDetails()) {
			total += item.getTotalPrice();
		}
		return total;
	}

	//Hàm định dạng chuyển từ number sang currency
	@Transient
	private float formatedTotalPrice;

	public String getFormatedTotalPrice() {
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		return vndFormat.format(this.getTotalPrice());
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
