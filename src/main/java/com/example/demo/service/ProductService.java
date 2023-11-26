package com.example.demo.service;

import com.example.demo.error.ProductError;
import com.example.demo.legacymodel.LegacyProduct;
import com.example.demo.newmodel.Product;
import com.example.demo.repository.ProductRepository;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Validation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vavr.API.Option;
import static io.vavr.API.Try;

@RequiredArgsConstructor
public class ProductService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;

  public Product saveProduct(final LegacyProduct legacyProduct) {
    final var map = Validation.combine(
            validateProductId(legacyProduct.getProductId()),
            validateColors(legacyProduct.getColorList()))
        .ap(ProductService::toProduct)
        .map(productRepository::save);
    return map
        .peekError(productErrors ->
            productErrors.map(ProductError::getMessage)
                .forEach(LOGGER::error))
        .getOrNull();
  }

  private static Validation<ProductError, Set<Integer>> validateColors(final java.util.Set<Integer> colors) {
    return Option(colors).toValidation(() -> ProductError.of("Color list is null"))
        .map(HashSet::ofAll);
  }

  private static Validation<ProductError, Long> validateProductId(final String productId) {
    return Option(productId).toValidation(() -> ProductError.of("Product ID is null"))
        .flatMap(notEmptyId ->
            Try(() -> Long.valueOf(notEmptyId))
                .toValidation(() -> ProductError.of("Product ID [" + notEmptyId + "] is not a number")));
  }

  private static Product toProduct(final Long productId, final Set<Integer> colors) {
    return Product.builder()
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
        LOGGER.error("Product ID [{}] is not a number", productId);
        return null;
      }
    } else {
      LOGGER.error("Product ID is null");
      return null;
    }

    java.util.Set<Integer> colorList = legacyProduct.getColorList();
    if (colorList == null) {
      LOGGER.error("Color list is null");
      return null;
    }

    Product product = toProduct(legacyProduct);
    return productRepository.save(product);
  }

  private static Product toProduct(final LegacyProduct legacyProduct) {
    return Product.builder()
        .id(Long.valueOf(legacyProduct.getProductId()))
        .colorIds(HashSet.ofAll(legacyProduct.getColorList()))
        .build();
  }
}
