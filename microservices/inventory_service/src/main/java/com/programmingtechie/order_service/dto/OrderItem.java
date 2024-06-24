package com.programmingtechie.order_service.dto;

import lombok.Data;

@Data
public class OrderItem {
	
	private String skuCode;
	private int quantity;

}
