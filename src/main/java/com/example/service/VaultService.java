package com.example.service;

import io.quarkus.vault.VaultKVSecretEngine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Map;
import org.jboss.logging.Logger;

/**
 * Service for HashiCorp Vault operations.
 */
@ApplicationScoped
public class VaultService {

  private static final Logger LOG = Logger.getLogger(VaultService.class);

  @Inject VaultKVSecretEngine kvSecretEngine;

  /**
   * Retrieves a secret from Vault.
   *
   * @param secretPath the path to the secret in Vault
   * @return a map containing the secret key-value pairs
   */
  public Map<String, String> getSecret(String secretPath) {
    LOG.infof("Retrieving secret from Vault: %s", secretPath);
    try {
      return kvSecretEngine.readSecret(secretPath);
    } catch (Exception e) {
      LOG.errorf("Failed to retrieve secret from Vault: %s", e.getMessage());
      throw new RuntimeException("Failed to retrieve secret from Vault", e);
    }
  }

  /**
   * Writes a secret to Vault.
   *
   * @param secretPath the path where the secret should be stored
   * @param secret the secret data as key-value pairs
   */
  public void writeSecret(String secretPath, Map<String, String> secret) {
    LOG.infof("Writing secret to Vault: %s", secretPath);
    try {
      kvSecretEngine.writeSecret(secretPath, secret);
      LOG.infof("Successfully wrote secret to Vault: %s", secretPath);
    } catch (Exception e) {
      LOG.errorf("Failed to write secret to Vault: %s", e.getMessage());
      throw new RuntimeException("Failed to write secret to Vault", e);
    }
  }

  /**
   * Deletes a secret from Vault.
   *
   * @param secretPath the path to the secret to delete
   */
  public void deleteSecret(String secretPath) {
    LOG.infof("Deleting secret from Vault: %s", secretPath);
    try {
      kvSecretEngine.deleteSecret(secretPath);
      LOG.infof("Successfully deleted secret from Vault: %s", secretPath);
    } catch (Exception e) {
      LOG.errorf("Failed to delete secret from Vault: %s", e.getMessage());
      throw new RuntimeException("Failed to delete secret from Vault", e);
    }
  }
}
