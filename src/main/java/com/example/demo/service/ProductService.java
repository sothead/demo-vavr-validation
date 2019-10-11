package com.example.demo.service;

import com.example.demo.error.ProductError;
import com.example.demo.legacymodel.LegacyProduct;
import com.example.demo.newmodel.Product;
import com.example.demo.repository.ProductRepository;
import io.vavr.collection.List;
import io.vavr.control.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vavr.API.Option;
import static io.vavr.API.Try;

public class ProductService {

  private static final Logger log = LoggerFactory.getLogger(ProductService.class);

  private ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product saveProduct(final LegacyProduct legacyProduct) {
    return Validation
        .combine(
            validateProdutId(legacyProduct.getProductId()),
            validateColors(legacyProduct)
        )
        .ap(ProductService::toProduct)
        .map(productRepository::save)
        .mapError(productErrors -> {
          productErrors.map(ProductError::getMessage)
              .forEach(log::error);
          return productErrors;
        })
        .getOrNull();
  }

  private Validation<ProductError, List<Integer>> validateColors(LegacyProduct legacyProduct) {
    return Option(legacyProduct.getColorList())
        .toValidation(() -> ProductError.builder().message("Color list is null").build());
  }

  private Validation<ProductError, Long> validateProdutId(String productId) {
    return Option(productId)
        .toValidation(() -> ProductError.builder().message("Product ID is null").build())
        .flatMap(
            notEmptyId -> Try(() -> Long.valueOf(notEmptyId))
                .toValidation(() -> ProductError.builder().message("Product ID [{" + notEmptyId + "}] is not a number").build())
        );
  }

  private static Product toProduct(final Long productId, final List<Integer> colors) {
    return
        Product
            .builder()
            .id(productId)
            .colorIds(colors)
            .build();
  }

  // legacy
  public Product oldSaveProduct(final LegacyProduct legacyProduct) {
    String productId = legacyProduct.getProductId();
    if (productId != null) {
      try {
        Long.valueOf(productId);
      } catch (NumberFormatException e) {
        log.error("Product ID [{}] is not a number", productId);
        return null;
      }
    } else {
      log.error("Product ID is null");
      return null;
    }

    List<Integer> colorList = legacyProduct.getColorList();
    if (colorList == null) {
      log.error("Color list is null");
      return null;
    }

    final Product product = toProduct(legacyProduct);
    return productRepository.save(product);
  }

  private static Product toProduct(final LegacyProduct legacyProduct) {
    return
        Product
            .builder()
            .id(Long.valueOf(legacyProduct.getProductId()))
            .colorIds(legacyProduct.getColorList())
            .build();
  }

}
