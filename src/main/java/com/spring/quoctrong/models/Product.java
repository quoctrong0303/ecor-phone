package com.spring.quoctrong.models;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.aspectj.weaver.tools.Trace;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(unique = true)
	private String name;

	@Column(columnDefinition = "LONGTEXT")
	private String introdution;
	private String thumbnail;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date CreatedDate;

	@ManyToOne
	@JoinColumn(name = "producerId")
	private Producer producer;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
	@ToString.Exclude // Khoonhg sử dụng trong toString()
	@JsonIgnore
	private Set<PromotionProduct> promotionProducts = new HashSet<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
	@ToString.Exclude // Khoonhg sử dụng trong toString()
	@JsonIgnore
	private Set<ProductOption> productOptions = new HashSet<>();

	public Product(String name, String introdution, String thumbnail) {
		super();
		this.name = name;
		this.introdution = introdution;
		this.thumbnail = thumbnail;
	}

	public Product(int id, String name, String introdution, String thumbnail, Producer producer) {
		super();
		this.id = id;
		this.name = name;
		this.introdution = introdution;
		this.thumbnail = thumbnail;
		this.producer = producer;
	}

	@Transient
	public String getFormatedDefaultPrice() {
		return this.getProductOptions().iterator().next().getFormatedPriceHasDiscount();
	}
	@Transient
	public String getFormatedDefaultPriceNoDiscount() {
		return this.getProductOptions().iterator().next().getFormatedPrice();
	}

	@Transient
	public Integer getDefaultDiscount() {
		if (this.getPromotionProducts().size() > 0) {
			for (PromotionProduct product : this.getPromotionProducts()) {
				if (product.getPromotion().getEndDate().isAfter(LocalDate.now())) {
					return product.getDiscount();
				}
			}
		}
		return 0;
	}
	
	@Transient
	public String getFormatedDefaultName() {
		if (this.getProductOptions().size() > 0) {
			return this.getName() + " " + this.getProductOptions().iterator().next().getRom();
		}
		return "Sản phẩm trống";
	}
	
	public Integer getDefaultProductOptionId() {
		if (this.getProductOptions().size() > 0) {
			return this.getProductOptions().iterator().next().getId();
		}
		return null;
	}
	
	

}
