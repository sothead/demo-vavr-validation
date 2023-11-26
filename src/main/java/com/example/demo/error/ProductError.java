package com.example.demo.error;

import lombok.Value;

@Value(staticConstructor = "of")
public class ProductError {

  String message;
}
