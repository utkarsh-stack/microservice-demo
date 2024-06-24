package com.programmingtechie.order_service.dto;

import java.util.List;

import com.programmingtechie.order_service.model.OrderLineItems;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {
	
	private Long id;
	private String orderNumber;
	private List<OrderLineItems> orderLineItems;
}
