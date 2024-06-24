package com.programmingtechie.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.order_service.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
