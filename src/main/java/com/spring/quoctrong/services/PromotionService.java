package com.spring.quoctrong.services;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.quoctrong.models.Producer;
import com.spring.quoctrong.models.Promotion;
import com.spring.quoctrong.repositories.PromotionRepository;

@Service
public class PromotionService {

	@Autowired
	PromotionRepository promotionRepository;
	
	public List<Promotion> findAll() {
		return promotionRepository.findAll();
	}
	
	public List<Promotion> findAllByOrderByNameAsc() {
		return promotionRepository.findAllByOrderByNameAsc();
	}
	
	public Promotion save(Promotion promotion) throws Exception{
		if (promotion.getStartDate().isAfter(promotion.getEndDate()) ) {
			throw new Exception("Ngày kết thúc phải lớn hơn ngày bắt đầu!");
		}
		if (promotion.getStartDate().isBefore(LocalDate.now())) {
			throw new Exception("Ngày bắt đầu phải lớn hơn hoặc bằng ngày hiện tại!");
		}
		return promotionRepository.save(promotion);
	}
	
	public Optional<Promotion> findById(Integer id) {
		return promotionRepository.findById(id);
	}
	
	public void deleteById(Integer id) throws Exception{
		if (!promotionRepository.existsById(id)) {
			throw new Exception("Không tìm thấy khuyến mãi!");
		}
		promotionRepository.deleteById(id);
	}
	
	public Promotion update(Integer id, Promotion newPromotion) throws Exception{
		Promotion promotion  = promotionRepository.findById(id)
				.orElseThrow(() -> new Exception("Không tìm thấy khuyến mãi!"));
		promotion.setName(newPromotion.getName());
		promotion.setContent(newPromotion.getContent());
		promotion.setStartDate(newPromotion.getStartDate());
		promotion.setEndDate(newPromotion.getEndDate());
		
		return promotionRepository.save(promotion);
	}
	
	public Promotion convertToModel(String promotion) {
		Promotion promotionModel = new Promotion();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		try {
			promotionModel = objectMapper.readValue(promotion, Promotion.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return promotionModel;
	}
}
