package infrastructure.repository;

import com.mongodb.reactivestreams.client.*;
import infrastructure.repository.provider.MongoProviders;
import io.micronaut.data.model.PersistentEntity;
import io.micronaut.data.mongodb.operations.MongoCollectionNameProvider;
import io.micronaut.data.mongodb.operations.MongoDatabaseNameProvider;
import io.micronaut.data.mongodb.operations.MongoReactorRepositoryOperations;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.function.BiFunction;
import org.bson.conversions.Bson;
import reactor.core.publisher.Mono;

public class CustomMongoRepository<E> {
  private final Class<E> type;
  private final MongoReactorRepositoryOperations repositoryOperations;
  private final MongoDatabaseNameProvider dbNameProvider;
  private final MongoCollectionNameProvider collectionNameProvider;

  public CustomMongoRepository(Class<E> type, MongoProviders mongoProviders) {
    this.type = type;
    this.repositoryOperations = mongoProviders.getMongoReactorRepositoryOperations();
    this.dbNameProvider = mongoProviders.getMongoDatabaseNameProvider();
    this.collectionNameProvider = mongoProviders.getMongoCollectionNameProvider();
  }

  public Mono<E> findAndUpdate(@NotNull Bson filter, @NotNull Bson update) {
    return withSessionAndCollection(
        (session, collection) -> executeFindAndUpdate(session, collection, filter, update));
  }

  private Mono<E> executeFindAndUpdate(
      ClientSession clientSession, MongoCollection<E> collection, Bson filter, Bson update) {
    Objects.requireNonNull(filter, "Filter options are required");
    Objects.requireNonNull(update, "Update options are required");
    return Mono.from(collection.findOneAndUpdate(clientSession, filter, update));
  }

  private Mono<E> withSessionAndCollection(
      BiFunction<ClientSession, MongoCollection<E>, Mono<E>> function) {
    return repositoryOperations.withClientSession(
        session -> function.apply(session, getCollection(session)));
  }

  private String getCollectionName() {
    return collectionNameProvider.provide(PersistentEntity.of(type));
  }

  private String getDatabaseName() {
    return dbNameProvider.provide(type);
  }

  private MongoCollection<E> getCollection(ClientSession clientSession) {
    MongoClient mongoClient = getClient(clientSession);
    MongoDatabase mongoDatabase = mongoClient.getDatabase(getDatabaseName());
    return mongoDatabase.getCollection(getCollectionName(), type);
  }

  private MongoClient getClient(ClientSession clientSession) {
    if (!(clientSession.getOriginator() instanceof MongoClient)) {
      throw new IllegalArgumentException("ClientSession originator is not a MongoClient");
    }
    return (MongoClient) clientSession.getOriginator();
  }
}
