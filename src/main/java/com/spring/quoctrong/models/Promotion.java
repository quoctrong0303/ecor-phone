package com.spring.quoctrong.models;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "promotions")
public class Promotion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private LocalDate startDate;
	private LocalDate endDate;
	private String name;
	private String content;
	
	@OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    @JsonIgnore
	private Set<PromotionProduct> promotionProducts = new HashSet<>();
}
