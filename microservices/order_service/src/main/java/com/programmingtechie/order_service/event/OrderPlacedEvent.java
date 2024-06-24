package com.programmingtechie.order_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderPlacedEvent {
	
	private String orderId;
	private String status;

}
