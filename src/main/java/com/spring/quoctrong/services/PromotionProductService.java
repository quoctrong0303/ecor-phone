package com.spring.quoctrong.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.Product;
import com.spring.quoctrong.models.Promotion;
import com.spring.quoctrong.models.PromotionProduct;
import com.spring.quoctrong.repositories.PromotionProductRepository;

@Service
public class PromotionProductService {

	@Autowired
	PromotionProductRepository promotionProductRepository;
	@Autowired
	PromotionService promotionService;
	@Autowired
	ProductServiceImpl productService;
	
	public PromotionProduct add(Integer promotionId, Integer productId, Integer discount) throws Exception{
		try {
			Promotion promotion = promotionService.findById(promotionId)
					.orElseThrow(() -> new Exception("Không tìm thấy khuyến mãi!"));
			Product product = productService.findProductById(productId)
					.orElseThrow(() -> new Exception("Không tìm thấy sản phẩm!"));
			Optional<PromotionProduct> promotionProduct2 = promotionProductRepository.findByProductId(productId);
			//Kiểm tra sản phẩm có khuyến mãi hay chưa
			if (promotionProduct2.isPresent() && promotionProduct2.get().getPromotion().getEndDate().isAfter(LocalDate.now())) {
				throw new Exception(("Sản phẩm này đang được áp dụng chương trình khuyến mãi khác!"));
			}
			PromotionProduct promotionProduct =new PromotionProduct(discount, product, promotion);
			return promotionProductRepository.save(promotionProduct);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public PromotionProduct save(PromotionProduct promotionProduct) throws Exception{
		List<PromotionProduct> promotionProducts = promotionProductRepository.findAll();
		if (promotionProduct.getProduct() == null) {
			throw new Exception("Không tìm thấy sản phẩm!");
		}
		if (promotionProduct.getPromotion() == null) {
			throw new Exception("Không tìm thấy khuyến mãi!");
		}
		//Nếu sản phẩm đã được khuyến mãi rồi
		for (PromotionProduct promotionProduct2 : promotionProducts) {
			if (promotionProduct2.getProduct().equals(promotionProduct.getProduct()) 
					&& promotionProduct2.getPromotion().getEndDate().isAfter(LocalDate.now())) {
				throw new Exception("Sản phảm này đang được áp dụng chương trình khuyến mãi khác!");
			}
		}
		return promotionProductRepository.save(promotionProduct);
	}
	
	public List<PromotionProduct> findAll() {
		return promotionProductRepository.findAll();
	}
	
	public PromotionProduct findById(Integer id) throws Exception{
		return promotionProductRepository.findById(id)
				.orElseThrow(() -> new Exception("Mã số không hợp lệ!"));
	}
	
	public Optional<PromotionProduct> findByProductId(Integer id) {
		return promotionProductRepository.findByProductId(id);
	}
	
	public PromotionProduct update(PromotionProduct promotionProduct) throws Exception{
		List<PromotionProduct> promotionProducts = promotionProductRepository.findAll();
		if (promotionProduct.getProduct() == null) {
			throw new Exception("Không tìm thấy sản phẩm!");
		}
		if (promotionProduct.getPromotion() == null) {
			throw new Exception("Không tìm thấy khuyến mãi!");
		}
		//Nếu sản phẩm đã được khuyến mãi rồi
		for (PromotionProduct promotionProduct2 : promotionProducts) {
			if (promotionProduct2.getProduct().equals(promotionProduct.getProduct())  
					&& promotionProduct2.getPromotion().getEndDate().isAfter(LocalDate.now())
					&& promotionProduct2.getId() != promotionProduct.getId()
					) {
				throw new Exception("Sản phảm này đang được áp dụng chương trình khuyến mãi khác!");
			}
		}
		return promotionProductRepository.save(promotionProduct);
	}
	
	public void deleteById(Integer id) throws Exception{
		if (!promotionProductRepository.existsById(id)) {
			throw new Exception("Không tìm thấy sản phẩm khuyến mãi!");
		}
		promotionProductRepository.deleteById(id);
	}
}
