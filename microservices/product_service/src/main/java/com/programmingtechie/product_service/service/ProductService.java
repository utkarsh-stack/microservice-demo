package com.programmingtechie.product_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.programmingtechie.product_service.dto.ProductRequest;
import com.programmingtechie.product_service.dto.ProductResponse;
import com.programmingtechie.product_service.model.Product;
import com.programmingtechie.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
//@RequiredArgsConstructor
@Slf4j
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder()
				.name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.build();
		productRepository.save(product);
		log.info("Product {} is saved", product.getId());
		
	}

	public List<ProductResponse> getAllProducts() {
		try {
			List<Product> products = productRepository.findAll();
			return products.stream().map(this::mapToProductResponse).toList();
		}catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
		log.info("Failed to fetch products from database server");
		return new ArrayList<ProductResponse>();
	}

	private ProductResponse mapToProductResponse(Product product) {
		log.info("Adding productId: {} to response", product.getId());
		return ProductResponse.builder()
				.name(product.getName())
				.id(product.getId())
				.description(product.getDescription())
				.price(product.getPrice())
				.build();
	}

}
