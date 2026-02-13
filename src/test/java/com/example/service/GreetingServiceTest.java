package com.example.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GreetingService.
 */
@QuarkusTest
class GreetingServiceTest {

  @Inject
  GreetingService greetingService;

  @Test
  void testGreet() {
    String name = "John";
    String result = greetingService.greet(name);

    assertNotNull(result);
    assertTrue(result.contains(name));
    assertTrue(result.contains("Hello"));
  }

  @Test
  void testGetDefaultGreeting() {
    String result = greetingService.getDefaultGreeting();

    assertNotNull(result);
    assertTrue(result.contains("Hello"));
  }
}
