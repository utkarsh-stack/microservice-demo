package com.programmingtechie.order_service.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.programmingtechie.order_service.dto.InventoryResponse;
import com.programmingtechie.order_service.dto.OrderDto;
import com.programmingtechie.order_service.dto.OrderLineItemsDto;
import com.programmingtechie.order_service.dto.OrderRequest;
import com.programmingtechie.order_service.event.OrderPlacedEvent;
import com.programmingtechie.order_service.model.Order;
import com.programmingtechie.order_service.model.OrderLineItems;
import com.programmingtechie.order_service.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private KafkaTemplate<String, OrderPlacedEvent> template;
	
	public String placeOrder(OrderRequest orderRequest) {
//		try {			
			List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream()
						.map(this::mapToOrderLineItems).toList();
			
			Order order = new Order();
			order.setOrderNumber(UUID.randomUUID().toString());
			order.setOrderLineItems(orderLineItemsList);
			
			List<String> skuCodes = order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).toList();
			List<Integer> quantity = order.getOrderLineItems().stream().map(OrderLineItems::getQuantity).toList();
			
			InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
					.uri("http://inventory-service/api/inventory/check",
							uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).queryParam("quantity", quantity).build())
					.retrieve()
					.bodyToMono(InventoryResponse[].class)
					.block();
			if(inventoryResponseArray.length<orderLineItemsList.size()) {
				OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(
						order.getOrderNumber(),
						"Failed");				
				template.send("OrderStatusTopic", orderPlacedEvent);
				throw new IllegalArgumentException("Some sku codes provided are not present or out of stock");
			}
			boolean allProductsInStock=Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::getIsInStock);
			
			if(allProductsInStock) {
				orderRepository.save(order);
				OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(
						order.getOrderNumber(),
						"Success");				
				template.send("OrderStatusTopic", orderPlacedEvent);
				log.info("Order placed with order number: {}", order.getOrderNumber());
				return order.getOrderNumber();
			}else {
				OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(
						order.getOrderNumber(),
						"Failed");				
				template.send("OrderStatusTopic", orderPlacedEvent);
				throw new IllegalArgumentException("Product is out of stock");
			}
//		}catch (Exception e) {
//			log.info(e.getMessage());
//			e.printStackTrace();
//			return e.getMessage();
//		}
	}
	
	public OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setId(orderLineItemsDto.getId());
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
		
//		;
//		return OrderLineItems.builder()
//				.id(orderLineItemsDto.getId())
//				.price(orderLineItemsDto.getPrice())
//				.quantity(orderLineItemsDto.getQuantity())
//				.skuCode(orderLineItemsDto.getSkuCode())
//				.build();		
	}
	public OrderDto mapToOrderDto(Order order) {
		return OrderDto.builder()
				.id(order.getId())
				.orderLineItems(order.getOrderLineItems())
				.orderNumber(order.getOrderNumber())
				.build();
	}

	public List<OrderDto> findAllOrders() {
		try {
			List<Order> orderList = orderRepository.findAll();
			log.info("All orders fetched from database");
			return orderList.stream().map(this::mapToOrderDto).toList();
		}catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return new ArrayList<OrderDto>();
	}

}
