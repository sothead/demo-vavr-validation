package com.example.demo.newmodel;

import io.vavr.collection.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Product {

  Long id;
  List<Integer> colorIds;
}
