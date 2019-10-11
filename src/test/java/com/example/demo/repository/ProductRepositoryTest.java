package com.example.demo.repository;

import com.example.demo.newmodel.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.vavr.API.List;
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
    Product product =
        Product
            .builder()
            .id(12345L)
            .colorIds(List(123, 456))
            .build();

    // WHEN
    Product result = productRepository.save(product);

    //THEN
    assertThat(result)
        .isEqualTo(
            Product
                .builder()
                .id(12345L)
                .colorIds(List(123, 456))
                .build()
        );
  }

}
