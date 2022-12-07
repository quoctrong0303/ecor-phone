package com.spring.quoctrong.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.models.Receipt;
import com.spring.quoctrong.models.ReceiptDetail;
@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Integer>{

	List<ReceiptDetail> findByReceiptAndProductOption(Receipt receipt, ProductOption productOption);
}
