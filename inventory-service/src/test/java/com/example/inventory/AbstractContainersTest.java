package com.example.inventory;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import java.util.Map;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.MountableFile;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractContainersTest implements TestPropertyProvider {

  static final GenericContainer<?> mongoDBContainer =
      new GenericContainer<>("mongo:latest")
          .withExposedPorts(27017)
          .withCopyFileToContainer(
              MountableFile.forClasspathResource("mongo-init.js"),
              "/docker-entrypoint-initdb.d/mongo-init.js");

  static {
    mongoDBContainer.start();
  }

  @Override
  public @NonNull Map<String, String> getProperties() {
    String mongoUri =
        String.format(
            "mongodb://%s:%d/inventory",
            mongoDBContainer.getHost(), mongoDBContainer.getMappedPort(27017));
    return Map.of("mongodb.uri", mongoUri);
  }
}
