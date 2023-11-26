package com.example.demo.repository;

import com.example.demo.newmodel.Product;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.vavr.API.Set;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductRepositoryTest {

  private ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    productRepository = new ProductRepository();
  }

  @Test
  void should_insert_then_return_product() {
    // GIVEN
    val product = Product.builder()
        .id(12345L)
        .colorIds(Set(123, 456))
        .build();

    // WHEN
    val result = productRepository.save(product);

    //THEN
    assertThat(result).isEqualTo(
        Product.builder()
            .id(12345L)
            .colorIds(Set(123, 456))
            .build());
  }

}
