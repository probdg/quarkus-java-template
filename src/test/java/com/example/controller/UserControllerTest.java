package com.example.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

/**
 * Integration tests for UserController.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

  private static Long createdUserId;

  @Test
  @Order(1)
  void testCreateUser() {
    String requestBody = "{\"email\": \"test@example.com\", \"name\": \"Test User\"}";

    Integer id = given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users")
        .then().statusCode(201).contentType(ContentType.JSON)
        .body("email", equalTo("test@example.com")).body("name", equalTo("Test User"))
        .body("id", notNullValue()).body("createdAt", notNullValue())
        .body("updatedAt", notNullValue()).extract().path("id");
    createdUserId = id.longValue();
  }

  @Test
  @Order(2)
  void testGetAllUsers() {
    given().when().get("/api/users").then().statusCode(200).contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(1));
  }

  @Test
  @Order(3)
  void testGetUserById() {
    if (createdUserId == null) {
      // Create a user first if not already created
      String requestBody = "{\"email\": \"test2@example.com\", \"name\": \"Test User 2\"}";
      Integer id = given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users")
          .then().statusCode(201).extract().path("id");
      createdUserId = id.longValue();
    }

    given().when().get("/api/users/{id}", createdUserId).then().statusCode(200)
        .contentType(ContentType.JSON).body("id", equalTo(createdUserId.intValue()))
        .body("email", notNullValue()).body("name", notNullValue());
  }

  @Test
  @Order(4)
  void testUpdateUser() {
    if (createdUserId == null) {
      // Create a user first if not already created
      String requestBody = "{\"email\": \"test3@example.com\", \"name\": \"Test User 3\"}";
      Integer id = given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users")
          .then().statusCode(201).extract().path("id");
      createdUserId = id.longValue();
    }

    String updateBody = "{\"email\": \"updated@example.com\", \"name\": \"Updated User\"}";

    given().contentType(ContentType.JSON).body(updateBody).when()
        .put("/api/users/{id}", createdUserId).then().statusCode(200).contentType(ContentType.JSON)
        .body("email", equalTo("updated@example.com")).body("name", equalTo("Updated User"));
  }

  @Test
  @Order(5)
  void testDeleteUser() {
    if (createdUserId == null) {
      // Create a user first if not already created
      String requestBody = "{\"email\": \"test4@example.com\", \"name\": \"Test User 4\"}";
      Integer id = given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users")
          .then().statusCode(201).extract().path("id");
      createdUserId = id.longValue();
    }

    given().when().delete("/api/users/{id}", createdUserId).then().statusCode(204);

    // Verify user is deleted
    given().when().get("/api/users/{id}", createdUserId).then().statusCode(404);
  }

  @Test
  void testCreateUserWithInvalidEmail() {
    String requestBody = "{\"email\": \"invalid-email\", \"name\": \"Test User\"}";

    given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users").then()
        .statusCode(400).contentType(ContentType.JSON);
  }

  @Test
  void testCreateUserWithBlankName() {
    String requestBody = "{\"email\": \"blank-name@example.com\", \"name\": \"  \"}";

    given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users").then()
        .statusCode(400).contentType(ContentType.JSON);
  }

  @Test
  void testGetNonExistentUser() {
    given().when().get("/api/users/{id}", 99999L).then().statusCode(404);
  }

  @Test
  void testUpdateNonExistentUser() {
    String updateBody = "{\"email\": \"updated@example.com\", \"name\": \"Updated User\"}";

    given().contentType(ContentType.JSON).body(updateBody).when().put("/api/users/{id}", 99999L)
        .then().statusCode(404);
  }

  @Test
  void testDeleteNonExistentUser() {
    given().when().delete("/api/users/{id}", 99999L).then().statusCode(404);
  }
}
