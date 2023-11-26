package com.example.demo.newmodel;

import io.vavr.collection.Set;
import lombok.Builder;
import lombok.Value;

import static io.vavr.API.Set;

@Builder
@Value
public class Product {

  Long id;
  @Builder.Default
  Set<Integer> colorIds = Set();
}
