package com.example.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
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
  @TestSecurity(user = "admin", roles = {"admin", "user"})
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
  @TestSecurity(user = "admin", roles = {"admin"})
  void testGetAllUsers() {
    given().when().get("/api/users").then().statusCode(200).contentType(ContentType.JSON)
        .body("size()", greaterThanOrEqualTo(1));
  }

  @Test
  @Order(3)
  @TestSecurity(user = "user", roles = {"user"})
  void testGetUserById() {
    if (createdUserId == null) {
      // Create a user first if not already created
      String requestBody = "{\"email\": \"test2@example.com\", \"name\": \"Test User 2\"}";
      Integer id = given().auth().preemptive().basic("admin", "admin").contentType(ContentType.JSON)
          .body(requestBody).when().post("/api/users").then().statusCode(201).extract().path("id");
      createdUserId = id.longValue();
    }

    given().when().get("/api/users/{id}", createdUserId).then().statusCode(200)
        .contentType(ContentType.JSON).body("id", equalTo(createdUserId.intValue()))
        .body("email", notNullValue()).body("name", notNullValue());
  }

  @Test
  @Order(4)
  @TestSecurity(user = "admin", roles = {"admin", "user"})
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
  @TestSecurity(user = "admin", roles = {"admin", "user"})
  void testDeleteUser() {
    if (createdUserId == null) {
      // Create a user first if not already created
      String requestBody = "{\"email\": \"test4@example.com\", \"name\": \"Test User 4\"}";
      Integer id = given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users")
          .then().statusCode(201).extract().path("id");
      createdUserId = id.longValue();
    }

    given().when().delete("/api/users/{id}", createdUserId).then().statusCode(204);

    // Verify user is deleted (should return 401 if not authenticated)
    given().when().get("/api/users/{id}", createdUserId).then().statusCode(anyOf(is(404), is(401)));
  }

  @Test
  @TestSecurity(user = "admin", roles = {"admin"})
  void testCreateUserWithInvalidEmail() {
    String requestBody = "{\"email\": \"invalid-email\", \"name\": \"Test User\"}";

    given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users").then()
        .statusCode(400).contentType(ContentType.JSON);
  }

  @Test
  @TestSecurity(user = "admin", roles = {"admin"})
  void testCreateUserWithBlankName() {
    String requestBody = "{\"email\": \"blank-name@example.com\", \"name\": \"  \"}";

    given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users").then()
        .statusCode(400).contentType(ContentType.JSON);
  }

  @Test
  @TestSecurity(user = "user", roles = {"user"})
  void testGetNonExistentUser() {
    given().when().get("/api/users/{id}", 99999L).then().statusCode(404);
  }

  @Test
  @TestSecurity(user = "admin", roles = {"admin"})
  void testUpdateNonExistentUser() {
    String updateBody = "{\"email\": \"updated@example.com\", \"name\": \"Updated User\"}";

    given().contentType(ContentType.JSON).body(updateBody).when().put("/api/users/{id}", 99999L)
        .then().statusCode(404);
  }

  @Test
  @TestSecurity(user = "admin", roles = {"admin"})
  void testDeleteNonExistentUser() {
    given().when().delete("/api/users/{id}", 99999L).then().statusCode(404);
  }

  // RBAC Tests

  @Test
  void testGetAllUsersWithoutAuth() {
    given().when().get("/api/users").then().statusCode(401);
  }

  @Test
  @TestSecurity(user = "user", roles = {"user"})
  void testGetAllUsersWithUserRole() {
    given().when().get("/api/users").then().statusCode(403);
  }

  @Test
  void testCreateUserWithoutAuth() {
    String requestBody = "{\"email\": \"noauth@example.com\", \"name\": \"No Auth User\"}";
    given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users").then()
        .statusCode(401);
  }

  @Test
  @TestSecurity(user = "user", roles = {"user"})
  void testCreateUserWithUserRole() {
    String requestBody = "{\"email\": \"useronly@example.com\", \"name\": \"User Only\"}";
    given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users").then()
        .statusCode(403);
  }

  @Test
  void testUpdateUserWithoutAuth() {
    String updateBody = "{\"email\": \"noauth@example.com\", \"name\": \"No Auth\"}";
    given().contentType(ContentType.JSON).body(updateBody).when().put("/api/users/{id}", 1L).then()
        .statusCode(401);
  }

  @Test
  @TestSecurity(user = "user", roles = {"user"})
  void testUpdateUserWithUserRole() {
    String updateBody = "{\"email\": \"useronly@example.com\", \"name\": \"User Only\"}";
    given().contentType(ContentType.JSON).body(updateBody).when().put("/api/users/{id}", 1L).then()
        .statusCode(403);
  }

  @Test
  void testDeleteUserWithoutAuth() {
    given().when().delete("/api/users/{id}", 1L).then().statusCode(401);
  }

  @Test
  @TestSecurity(user = "user", roles = {"user"})
  void testDeleteUserWithUserRole() {
    given().when().delete("/api/users/{id}", 1L).then().statusCode(403);
  }

  @Test
  void testGetUserByIdWithoutAuth() {
    given().when().get("/api/users/{id}", 1L).then().statusCode(401);
  }

  @Test
  @TestSecurity(user = "guest", roles = {"guest"})
  void testGetUserByIdWithGuestRole() {
    given().when().get("/api/users/{id}", 1L).then().statusCode(403);
  }
}
