package com.programmingtechie.order_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.order_service.dto.InventoryDto;
import com.programmingtechie.order_service.dto.InventoryResponse;
import com.programmingtechie.order_service.dto.OrderItem;
import com.programmingtechie.order_service.model.Inventory;
import com.programmingtechie.order_service.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	@Autowired
	InventoryService inventoryService;
	
	@GetMapping("/check")
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode, @RequestParam List<Integer> quantity) {
		return inventoryService.checkQuantity(skuCode, quantity);
		
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String saveInventory(@RequestBody Inventory inventory) {
		return inventoryService.saveInventory(inventory);
		
	}
	
	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryDto> findAll(){
		return inventoryService.findAll();
		
	}

}
