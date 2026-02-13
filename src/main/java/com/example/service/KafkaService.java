package com.example.service;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.inject.Inject;

/**
 * Service for Kafka messaging operations.
 */
@ApplicationScoped
@Slf4j
public class KafkaService {

  @Inject
  @Channel("greetings-out")
  Emitter<String> greetingEmitter;

  /**
   * Send a message to Kafka topic.
   */
  public void sendGreeting(String message) {
    log.info("Sending message to Kafka: {}", message);
    greetingEmitter.send(message);
  }

  /**
   * Consume messages from Kafka topic.
   */
  @Incoming("greetings-in")
  public void receiveGreeting(String message) {
    log.info("Received message from Kafka: {}", message);
  }
}
