package com.example.controller;

import com.example.service.VaultService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller demonstrating RBAC with role-based security.
 */
@Path("/api/secured")
@Slf4j
public class SecuredController {

  @Inject
  VaultService vaultService;

  @GET
  @Path("/public")
  @PermitAll
  @Produces(MediaType.TEXT_PLAIN)
  public String publicEndpoint() {
    log.info("Public endpoint accessed");
    return "This is a public endpoint - accessible to everyone";
  }

  @GET
  @Path("/user")
  @RolesAllowed({"user", "admin"})
  @Produces(MediaType.TEXT_PLAIN)
  public String userEndpoint() {
    log.info("User endpoint accessed");
    return "This endpoint requires 'user' or 'admin' role";
  }

  @GET
  @Path("/admin")
  @RolesAllowed("admin")
  @Produces(MediaType.TEXT_PLAIN)
  public String adminEndpoint() {
    log.info("Admin endpoint accessed");
    return "This endpoint requires 'admin' role";
  }

  @GET
  @Path("/vault-secret")
  @PermitAll
  @Produces(MediaType.APPLICATION_JSON)
  public Response getVaultSecret() {
    log.info("Vault secret endpoint accessed");
    try {
      // Example: retrieve a secret from Vault
      Map<String, String> secret = vaultService.getSecret("config/app");
      return Response.ok(secret).build();
    } catch (Exception e) {
      log.error("Failed to retrieve secret from Vault", e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(Map.of("error", "Failed to retrieve secret from Vault: " + e.getMessage()))
          .build();
    }
  }
}
