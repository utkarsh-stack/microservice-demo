package com.programmingtechie.product_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.programmingtechie.product_service.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
