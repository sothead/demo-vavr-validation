package com.example.demo.service;

import com.example.demo.legacymodel.LegacyProduct;
import com.example.demo.newmodel.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.vavr.API.List;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceTest {

  private ProductService productService;

  private ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    productRepository = new ProductRepository();
    productService = new ProductService(productRepository);
  }

  @Test
  void should_save_and_return_product_when_valid_legacyProduct() {
    // GIVEN
    LegacyProduct legacyProduct =
        LegacyProduct
            .builder()
            .productId("12345")
            .colorList(List(123, 456))
            .build();

    // WHEN
    Product result = productService.saveProduct(legacyProduct);

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

  @Test
  void should_return_null_when_product_id_null() {
    // GIVEN
    LegacyProduct legacyProduct = LegacyProduct.builder().build();

    // WHEN
    Product result = productService.saveProduct(legacyProduct);

    //THEN
    assertThat(result).isNull();
  }

  @Test
  void should_return_null_when_product_id_is_not_a_number() {
    // GIVEN
    LegacyProduct legacyProduct =
        LegacyProduct
            .builder()
            .productId("FAKE_ID")
            .colorList(List(123, 456))
            .build();

    // WHEN
    Product result = productService.saveProduct(legacyProduct);

    //THEN
    assertThat(result).isNull();
  }


  @Test
  void should_return_null_when_color_ids_null() {
    // GIVEN
    LegacyProduct legacyProduct =
        LegacyProduct
            .builder()
            .productId("1234")
            .build();

    // WHEN
    Product result = productService.saveProduct(legacyProduct);

    //THEN
    assertThat(result).isNull();
  }

}
