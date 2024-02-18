package infrastructure.repository.provider;

import io.micronaut.data.mongodb.operations.MongoCollectionNameProvider;
import io.micronaut.data.mongodb.operations.MongoDatabaseNameProvider;
import io.micronaut.data.mongodb.operations.MongoReactorRepositoryOperations;
import jakarta.inject.Singleton;

@Singleton
public class MongoProvidersBean implements MongoProviders {

  private final MongoReactorRepositoryOperations mongoReactorRepositoryOperations;
  private final MongoDatabaseNameProvider mongoDatabaseNameProvider;
  private final MongoCollectionNameProvider mongoCollectionNameProvider;

  public MongoProvidersBean(
      MongoReactorRepositoryOperations mongoReactorRepositoryOperations,
      MongoDatabaseNameProvider mongoDatabaseNameProvider,
      MongoCollectionNameProvider mongoCollectionNameProvider) {
    this.mongoReactorRepositoryOperations = mongoReactorRepositoryOperations;
    this.mongoDatabaseNameProvider = mongoDatabaseNameProvider;
    this.mongoCollectionNameProvider = mongoCollectionNameProvider;
  }

  public MongoReactorRepositoryOperations getMongoReactorRepositoryOperations() {
    return mongoReactorRepositoryOperations;
  }

  public MongoDatabaseNameProvider getMongoDatabaseNameProvider() {
    return mongoDatabaseNameProvider;
  }

  public MongoCollectionNameProvider getMongoCollectionNameProvider() {
    return mongoCollectionNameProvider;
  }
}
