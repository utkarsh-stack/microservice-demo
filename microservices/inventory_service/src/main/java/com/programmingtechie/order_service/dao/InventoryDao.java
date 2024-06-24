package com.programmingtechie.order_service.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.order_service.model.Inventory;

public interface InventoryDao extends JpaRepository<Inventory, Long> {

	Optional<Inventory> findBySkuCode(String skuCode);

	List<Inventory> findBySkuCodeIn(List<String> skuCode);

}
