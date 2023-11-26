package com.example.demo.service;

import com.example.demo.legacymodel.LegacyProduct;
import com.example.demo.newmodel.Product;
import com.example.demo.repository.ProductRepository;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static io.vavr.API.Set;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceTest {

  private ProductService productService;

  @BeforeEach
  void setUp() {
    productService = new ProductService(new ProductRepository());
  }

  @Test
  void should_save_and_return_product_when_valid_legacyProduct() {
    // GIVEN
    val legacyProduct = LegacyProduct.builder()
        .productId("12345")
        .colorList(Set.of(123, 456))
        .build();

    // WHEN
    val result = productService.saveProduct(legacyProduct);

    //THEN
    assertThat(result).isEqualTo(
        Product.builder()
            .id(12345L)
            .colorIds(Set(123, 456))
            .build());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   ", "NaN"})
  void should_return_null_when_invalid_product_id(final String productId) {
    // GIVEN
    val legacyProduct = LegacyProduct.builder()
        .productId(productId)
        .build();

    // WHEN
    val result = productService.saveProduct(legacyProduct);

    //THEN
    assertThat(result).isNull();
  }

  @Test
  void should_return_null_when_color_ids_null() {
    // GIVEN
    val legacyProduct = LegacyProduct
        .builder()
        .productId("1234")
        .build();

    // WHEN
    val result = productService.saveProduct(legacyProduct);

    //THEN
    assertThat(result).isNull();
  }

}
