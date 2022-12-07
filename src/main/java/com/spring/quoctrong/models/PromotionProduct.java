package com.spring.quoctrong.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "promotions_products")
public class PromotionProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int discount;
	
	@ManyToOne
	@JoinColumn(name = "productId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    @JsonIgnore
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "promotionId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    @JsonIgnore
	private Promotion promotion;

	public PromotionProduct(int discount, Product product, Promotion promotion) {
		super();
		this.discount = discount;
		this.product = product;
		this.promotion = promotion;
	}
	
	
}
