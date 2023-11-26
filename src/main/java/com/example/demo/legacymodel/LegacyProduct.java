package com.example.demo.legacymodel;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class LegacyProduct {

  String productId;
  Set<Integer> colorList;
}
