package com.programmingtechie.notificationService.kafkaListner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
	
	private String orderId;
	private String status;

}