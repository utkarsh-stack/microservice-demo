package com.programmingtechie.notificationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import com.programmingtechie.notificationService.kafkaListner.OrderPlacedEvent;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {
	public static void main(String[] args) {
			
			
			SpringApplication.run(NotificationServiceApplication.class, args);
		}
	
		@KafkaListener(topics="OrderStatusTopic")
		public void sendNotification(OrderPlacedEvent orderPlacedEvent) {
			log.info("Order status for {}: {}", orderPlacedEvent.getOrderId(), orderPlacedEvent.getStatus());
			Twilio.init("AC59f68812dadeb8681c938707a4950332", "9ffe64618f39d4ba1e5a3b6a56134cdf");
			if(orderPlacedEvent.getStatus().equalsIgnoreCase("Success"))
				Message.creator(new PhoneNumber("whatsapp:+918709685599"), new PhoneNumber("whatsapp:+14155238886"), "Placed order").create();
			else
				Message.creator(new PhoneNumber("whatsapp:+918709685599"), new PhoneNumber("whatsapp:+14155238886"), "Failed to place order").create();
		}
//		@KafkaListener(topics="OrderStatusTopic")
//		public void sendNotification(String message) {
//			log.info("Message received: {}", message);
//		}

}
