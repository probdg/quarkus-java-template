package com.example.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for VaultService.
 *
 * Note: These tests are disabled by default as they require a running Vault instance. To run these
 * tests, start Vault using docker-compose and remove the @Disabled annotation.
 */
@QuarkusTest
class VaultServiceTest {

  @Inject
  VaultService vaultService;

  @Test
  @Disabled("Requires running Vault instance")
  void testWriteAndReadSecret() {
    // Given
    String secretPath = "test/credentials";
    Map<String, String> secretData = Map.of("username", "testuser", "password", "testpass");

    // When
    vaultService.writeSecret(secretPath, secretData);
    Map<String, String> retrievedSecret = vaultService.getSecret(secretPath);

    // Then
    assertNotNull(retrievedSecret);
    assertEquals("testuser", retrievedSecret.get("username"));
    assertEquals("testpass", retrievedSecret.get("password"));
  }

  @Test
  @Disabled("Requires running Vault instance")
  void testDeleteSecret() {
    // Given
    String secretPath = "test/temp-secret";
    Map<String, String> secretData = Map.of("key", "value");

    // When
    vaultService.writeSecret(secretPath, secretData);
    vaultService.deleteSecret(secretPath);

    // Then
    assertThrows(RuntimeException.class, () -> vaultService.getSecret(secretPath));
  }
}
