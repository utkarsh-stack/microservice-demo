
package com.programmingtechie.order_service.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLineItemsDto {
	
	private Long id;
	private String skuCode;
	private BigDecimal price;
	private Integer quantity;

}
