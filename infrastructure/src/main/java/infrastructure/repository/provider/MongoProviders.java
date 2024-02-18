package infrastructure.repository.provider;

import io.micronaut.data.mongodb.operations.MongoCollectionNameProvider;
import io.micronaut.data.mongodb.operations.MongoDatabaseNameProvider;
import io.micronaut.data.mongodb.operations.MongoReactorRepositoryOperations;

public interface MongoProviders {

  MongoReactorRepositoryOperations getMongoReactorRepositoryOperations();

  MongoDatabaseNameProvider getMongoDatabaseNameProvider();

  MongoCollectionNameProvider getMongoCollectionNameProvider();
}
