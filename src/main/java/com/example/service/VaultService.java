package com.example.service;

import io.quarkus.arc.DefaultBean;
import io.quarkus.vault.VaultKVSecretEngine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.Map;
import org.jboss.logging.Logger;

/**
 * Service for HashiCorp Vault operations. This service is only available when Vault is configured
 * and available.
 */
@ApplicationScoped
@DefaultBean
public class VaultService {

  private static final Logger LOG = Logger.getLogger(VaultService.class);

  @Inject
  Instance<VaultKVSecretEngine> kvSecretEngine;

  /**
   * Retrieves a secret from Vault.
   *
   * @param secretPath the path to the secret in Vault
   * @return a map containing the secret key-value pairs
   */
  public Map<String, String> getSecret(String secretPath) {
    if (!kvSecretEngine.isResolvable()) {
      LOG.warn("Vault is not available. Cannot retrieve secret: " + secretPath);
      throw new RuntimeException("Vault is not configured or available");
    }

    LOG.infof("Retrieving secret from Vault: %s", secretPath);
    try {
      return kvSecretEngine.get().readSecret(secretPath);
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
    if (!kvSecretEngine.isResolvable()) {
      LOG.warn("Vault is not available. Cannot write secret: " + secretPath);
      throw new RuntimeException("Vault is not configured or available");
    }

    LOG.infof("Writing secret to Vault: %s", secretPath);
    try {
      kvSecretEngine.get().writeSecret(secretPath, secret);
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
    if (!kvSecretEngine.isResolvable()) {
      LOG.warn("Vault is not available. Cannot delete secret: " + secretPath);
      throw new RuntimeException("Vault is not configured or available");
    }

    LOG.infof("Deleting secret from Vault: %s", secretPath);
    try {
      kvSecretEngine.get().deleteSecret(secretPath);
      LOG.infof("Successfully deleted secret from Vault: %s", secretPath);
    } catch (Exception e) {
      LOG.errorf("Failed to delete secret from Vault: %s", e.getMessage());
      throw new RuntimeException("Failed to delete secret from Vault", e);
    }
  }

  /**
   * Checks if Vault is available.
   *
   * @return true if Vault is configured and available, false otherwise
   */
  public boolean isVaultAvailable() {
    return kvSecretEngine.isResolvable();
  }
}
