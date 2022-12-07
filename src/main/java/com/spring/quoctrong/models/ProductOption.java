package com.spring.quoctrong.models;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "product_options")
public class ProductOption {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String color;
	private String rom;
	private String ram;
	private String monitor;
	private String frontCamera;
	private String rearCamera;
	private String cpu;
	private String gpu;
	private String pin;
	@Column(columnDefinition = "TEXT")
	private String allSpecifications;
	private float sellingPrice;
	private float costPrice;

	@ManyToOne
	@JoinColumn(name = "productId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
	@ToString.Exclude // Khoonhg sử dụng trong toString()
	@JsonIgnore
	private Product product;

	public ProductOption(String color, String rom, String ram, String monitor, String frontCamera, String rearCamera,
			String cpu, String gpu, String pin, String allSpecifications, float sellingPrice, float costPrice,
			Product product) {
		super();
		this.color = color;
		this.rom = rom;
		this.ram = ram;
		this.monitor = monitor;
		this.frontCamera = frontCamera;
		this.rearCamera = rearCamera;
		this.cpu = cpu;
		this.gpu = gpu;
		this.pin = pin;
		this.allSpecifications = allSpecifications;
		this.sellingPrice = sellingPrice;
		this.costPrice = costPrice;
		this.product = product;
	}

	@OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
	@ToString.Exclude // Khoonhg sử dụng trong toString()
	@JsonIgnore
	private Set<ReceiptDetail> receiptDetails = new HashSet<>();
	
	@OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
	@ToString.Exclude // Khoonhg sử dụng trong toString()
	@JsonIgnore
	private Set<OrderDetail> orderDetails = new HashSet<>();
	

	@OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
	@ToString.Exclude // Khoonhg sử dụng trong toString().
	@JsonIgnore
	private Set<ProductImage> productImages = new HashSet<>();

	@OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
	@ToString.Exclude // Khoonhg sử dụng trong toString()
	@JsonIgnore
	private Set<Stock> stocks = new HashSet<>();

	@Transient
	public String getDisplayName() {
		String name = this.getProduct().getName() + ' ' + '-' + ' ' + this.getColor() + ' ' + '-' + ' ' + this.getRom();
		return name;
	}

	@Transient
	public String getFormatedPrice() {
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		return vndFormat.format(this.getSellingPrice());
	}

	@Transient
	public Double getPriceHasDiscount() {
		if (this.isPromoted()) {
			return this.getSellingPrice() * (double) ((100 - this.getProduct().getDefaultDiscount()) / (double) 100);
		}
		return (double) this.sellingPrice;
	}

	@Transient
	public String getFormatedPriceHasDiscount() {
		Locale vn = new Locale("vi", "VN");
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vn);
		if (this.isPromoted()) {
			return vndFormat.format(
					this.getSellingPrice() * (double) ((100 - this.getProduct().getDefaultDiscount()) / (double) 100));
		}
		return vndFormat.format(this.getSellingPrice());
	}

	@Transient
	public boolean isPromoted() {
		if (this.getProduct().getPromotionProducts().size() > 0) {
			for (PromotionProduct promotionProduct : this.getProduct().getPromotionProducts()) {
				if (promotionProduct.getPromotion().getEndDate().isAfter(LocalDate.now())) {
					return true;
				}
			}
		}
		return false;
	}

	@Transient
	public Promotion getCurrentPromotion() {
		if (this.isPromoted()) {
			for (PromotionProduct promotionProduct : this.getProduct().getPromotionProducts()) {
				if (promotionProduct.getPromotion().getEndDate().isAfter(LocalDate.now())) {
					return promotionProduct.getPromotion();
				}
			}
		}
		return null;
	}

}
