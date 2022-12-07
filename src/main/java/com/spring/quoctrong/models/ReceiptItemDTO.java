package com.spring.quoctrong.models;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptItemDTO {

	private int productOptionId;
	private int quantity;
	private float price;
	
	
}
