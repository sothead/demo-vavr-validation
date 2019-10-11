package com.example.demo.repository;

import com.example.demo.newmodel.Product;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ProductRepository {

  private static final Map<Long, Product> PRODUCTS = new HashMap<>();

  public Product save(Product product) {
    PRODUCTS.put(product.getId(), product);
    return product;
  }
}
