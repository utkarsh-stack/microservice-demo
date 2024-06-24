package com.programmingtechie.order_service.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderRequest {
	
	private List<OrderLineItemsDto> orderLineItemsDtoList;

}
