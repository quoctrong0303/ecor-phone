package com.spring.quoctrong.models;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class ProductWrapper {
	Product product;
	ProductOption productOption;
	MultipartFile[] productImages;
}
