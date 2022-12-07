package com.spring.quoctrong.models;

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
@Table(name = "product_images")
public class ProductImage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String url;

	@ManyToOne
	@JoinColumn(name = "productOptionId")
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
	private ProductOption productOption;
	
	public ProductImage(String url, ProductOption productOption) {
		super();
		this.url = url;
		this.productOption = productOption;
	}
}
