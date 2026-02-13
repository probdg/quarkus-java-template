package com.example.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

/**
 * Integration tests for GreetingController.
 */
@QuarkusTest
class GreetingControllerTest {

  @Test
  void testGetDefaultGreeting() {
    given().when().get("/api/greeting").then().statusCode(200).contentType(ContentType.JSON)
        .body("message", notNullValue()).body("timestamp", notNullValue());
  }

  @Test
  void testGetGreetingByName() {
    String name = "John";

    given().when().get("/api/greeting/{name}", name).then().statusCode(200)
        .contentType(ContentType.JSON).body("message", containsString(name))
        .body("timestamp", notNullValue());
  }

  @Test
  void testGetGreetingByNameWithBlankName() {
    // Note: Spaces in path parameters get URL encoded and may be treated as valid
    // This test verifies that blank/whitespace-only names still get validated
    String name = "   ";

    given().when().get("/api/greeting/{name}", name).then().statusCode(400);
  }

  @Test
  void testCreateGreeting() {
    String requestBody = "{\"name\": \"Alice\"}";

    given().contentType(ContentType.JSON).body(requestBody).when().post("/api/greeting").then()
        .statusCode(201).contentType(ContentType.JSON).body("message", containsString("Alice"))
        .body("timestamp", notNullValue());
  }

  @Test
  void testCreateGreetingWithInvalidRequest() {
    String requestBody = "{\"name\": \"A\"}";

    given().contentType(ContentType.JSON).body(requestBody).when().post("/api/greeting").then()
        .statusCode(400).contentType(ContentType.JSON);
  }

  @Test
  void testCreateGreetingWithBlankName() {
    String requestBody = "{\"name\": \"  \"}";

    given().contentType(ContentType.JSON).body(requestBody).when().post("/api/greeting").then()
        .statusCode(400).contentType(ContentType.JSON);
  }
}
