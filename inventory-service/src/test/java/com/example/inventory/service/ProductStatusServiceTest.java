package com.example.inventory.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.inventory.entity.ProductStatusEntity;
import com.example.inventory.repository.ProductStatusRepository;
import com.example.models.ProductStatus;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Inject;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
class ProductStatusServiceTest {

  @Inject ProductStatusService productStatusService;

  @Test
  void findAll() {
    Flowable<ProductStatus> productStatusFlowable = productStatusService.findAll();
    productStatusFlowable.test().assertValueCount(2);
  }

  @MockBean(ProductStatusRepository.class)
  ProductStatusRepository productStatusRepository() {
    ProductStatusRepository productStatusRepository = mock(ProductStatusRepository.class);
    Flowable<ProductStatusEntity> productStatusEntityFlowable =
        Flowable.fromIterable(getProductStatusEntities());
    when(productStatusRepository.findAll()).thenReturn(productStatusEntityFlowable);
    return productStatusRepository;
  }

  private List<ProductStatusEntity> getProductStatusEntities() {
    return List.of(
        new ProductStatusEntity(new ObjectId(createObjectId("1")), 1),
        new ProductStatusEntity(new ObjectId(createObjectId("2")), 2));
  }

  private String createObjectId(String id) {
    if (id.length() > 24) {
      throw new IllegalArgumentException("Id length must be 24 characters or less.");
    }
    StringBuilder sb = new StringBuilder(id);
    while (sb.length() < 24) {
      sb.insert(0, "0");
    }
    return sb.toString();
  }
}
