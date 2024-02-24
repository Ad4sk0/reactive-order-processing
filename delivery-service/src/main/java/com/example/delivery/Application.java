package com.example.delivery;

import io.micronaut.runtime.Micronaut;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;

public class Application {

  public static void main(String[] args) throws Exception {
    EmbeddedActiveMQ embedded = new EmbeddedActiveMQ();
    embedded.start();
    Micronaut.run(Application.class, args);
  }
}
