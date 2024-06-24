package com.programmingtechie.order_service.dto;

import lombok.Data;

@Data
public class InventoryResponse {
	
	private String skuCode;
	private Boolean isInStock;

}
