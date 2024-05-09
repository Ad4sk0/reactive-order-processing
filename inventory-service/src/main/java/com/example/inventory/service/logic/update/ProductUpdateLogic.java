package com.example.inventory.service.logic.update;

import com.example.inventory.entity.ProductEntity;
import com.example.inventory.mapper.ProductStatusMapper;
import com.example.inventory.repository.ProductRepository;
import com.example.models.Product;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
public class ProductUpdateLogic {

  private final ProductRepository productRepository;

  public ProductUpdateLogic(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Mono<ProductEntity> updateProduct(Product product) {
    if (product.id() == null) {
      return Mono.error(new ValidationException("Product id is required"));
    }

    return productRepository
        .findById(new ObjectId(product.id()))
        .switchIfEmpty(
            Mono.error(
                new ValidationException(
                    String.format("Product with id %s does not exist", product.id()))))
        .flatMap(
            existingProduct -> {
              ProductEntity productEntityToUpdate = updateAllowedFields(existingProduct, product);
              return productRepository.update(productEntityToUpdate);
            });
  }

  public Flux<ProductEntity> updateProducts(List<Product> products) {
    return productRepository
        .findByIds(products.stream().map(Product::id).map(ObjectId::new).toList())
        .collectList()
        .flatMapMany(
            existingProducts -> {
              if (existingProducts.size() != products.size()) {
                return Flux.error(
                    new ValidationException(
                        "Unable to update products. Some of the products were not found"));
              }
              return Flux.fromIterable(updateAllowedFields(existingProducts, products));
            })
        .collectList()
        .flatMapMany(productRepository::updateAll);
  }

  private ProductEntity updateAllowedFields(ProductEntity existingProduct, Product product) {
    existingProduct.setName(product.name());
    existingProduct.setProductType(product.productType());
    existingProduct.setStatus(ProductStatusMapper.toEntity(product.status()));
    return existingProduct;
  }

  private List<ProductEntity> updateAllowedFields(
      List<ProductEntity> existingProducts, List<Product> products) {
    Map<String, Product> productMap =
        products.stream().collect(Collectors.toMap(Product::id, product -> product));

    return existingProducts.stream()
        .map(
            existingProductEntity -> {
              Product product = productMap.get(existingProductEntity.getId().toString());
              return updateAllowedFields(existingProductEntity, product);
            })
        .toList();
  }
}
