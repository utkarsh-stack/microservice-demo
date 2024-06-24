package com.programmingtechie.order_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.programmingtechie.order_service.dao.InventoryDao;
import com.programmingtechie.order_service.dto.InventoryDto;
import com.programmingtechie.order_service.dto.InventoryResponse;
import com.programmingtechie.order_service.dto.OrderItem;
import com.programmingtechie.order_service.model.Inventory;

@Service
public class InventoryService {
	
	@Autowired
	private InventoryDao inventoryDao;
	
	public List<InventoryResponse> checkQuantity(List<String> skuCode, List<Integer> quantity){
		List<String> skuCodes = new ArrayList<String>();
		if(skuCode.size()==quantity.size())
		{
			for(int i=0;i<skuCode.size();i++){
				String skuCodeTemp=skuCode.get(i);
				if(inventoryDao.findBySkuCode(skuCodeTemp).get().getQuantity()>=quantity.get(i))
					skuCodes.add(skuCodeTemp);
			}
		}
		return inventoryDao.findBySkuCodeIn(skuCodes).stream()
				.map(this::mapToInventoryResponse).toList();
	}

	public String saveInventory(Inventory inventory) {
		try {
			inventoryDao.save(inventory);
			return inventory.getSkuCode();
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Failed to save";
		}
	}
	public InventoryResponse mapToInventoryResponse(Inventory inventory) {
		InventoryResponse inventoryResponse= new InventoryResponse();
		if(inventory==null) {
			inventoryResponse.setIsInStock(false);
			return inventoryResponse;
		}
		inventoryResponse.setSkuCode(inventory.getSkuCode());
		inventoryResponse.setIsInStock(inventory.getQuantity()>0);
		return inventoryResponse;
	}

	public List<InventoryDto> findAll() {
		return inventoryDao.findAll().stream().map(this::mapToInventoryDto).toList();
	}
	
	public InventoryDto mapToInventoryDto(Inventory inventory) {
		InventoryDto inventoryDto = new InventoryDto();
		if(inventory==null) {
			return inventoryDto;
		}
		inventoryDto.setSkuCode(inventory.getSkuCode());
		inventoryDto.setQuantity(inventory.getQuantity());
		return inventoryDto;
	}

}
