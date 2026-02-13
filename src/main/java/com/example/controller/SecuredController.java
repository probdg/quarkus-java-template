package com.example.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller demonstrating RBAC with role-based security.
 */
@Path("/api/secured")
@Slf4j
public class SecuredController {

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
}
