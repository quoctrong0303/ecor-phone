package com.spring.quoctrong.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {
	private String income;
	private String cost;
	private Integer soldProduct;
	private String profit;
}
