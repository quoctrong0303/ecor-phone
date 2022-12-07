package com.spring.quoctrong.services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.quoctrong.models.ReceiptItemDTO;

@Service
public class ReceiptItemDTOService {

	public ReceiptItemDTO convertToModel(String str) {
		ReceiptItemDTO model = new ReceiptItemDTO();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model = objectMapper.readValue(str, ReceiptItemDTO.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return model;
	}
}
