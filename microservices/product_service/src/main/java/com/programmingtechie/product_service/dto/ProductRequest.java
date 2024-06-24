package com.programmingtechie.product_service.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequest {
	
	private String name;
	private String description;
	private BigDecimal price;

}
