package com.example.delivery.service;

import com.example.delivery.mapper.DeliveryMapper;
import com.example.delivery.repository.DeliveryRepository;
import com.example.models.Delivery;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Singleton;
import org.bson.types.ObjectId;

@Singleton
public class DeliveryServiceImpl implements DeliveryService {

  private final DeliveryRepository deliveryRepository;

  public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
    this.deliveryRepository = deliveryRepository;
  }

  @Override
  public Flowable<Delivery> findAll() {
    return Flowable.fromPublisher(deliveryRepository.findAll()).map(DeliveryMapper::toDTO);
  }

  @Override
  public Single<Delivery> save(Delivery delivery) {
    if (delivery.id() == null) {
      return Single.fromPublisher(deliveryRepository.save(DeliveryMapper.toEntity(delivery)))
          .map(DeliveryMapper::toDTO);
    } else {
      return Single.fromPublisher(deliveryRepository.update(DeliveryMapper.toEntity(delivery)))
          .map(DeliveryMapper::toDTO);
    }
  }

  @Override
  public Maybe<Delivery> findById(String id) {
    return Maybe.fromPublisher(deliveryRepository.findById(new ObjectId(id)))
        .map(DeliveryMapper::toDTO);
  }
}
