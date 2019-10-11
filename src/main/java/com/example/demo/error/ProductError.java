package com.example.demo.error;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ProductError {

  String message;
}
