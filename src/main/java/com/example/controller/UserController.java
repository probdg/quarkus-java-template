package com.example.controller;

import com.example.dto.UserRequest;
import com.example.dto.UserResponse;
import com.example.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.util.List;

/**
 * REST controller for user CRUD operations.
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "User CRUD operations")
public class UserController {

  private static final Logger LOG = Logger.getLogger(UserController.class);

  @Inject
  UserService userService;

  /**
   * Get all users.
   *
   * @return list of users
   */
  @GET
  @RolesAllowed("admin")
  @Operation(summary = "Get all users", description = "Returns a list of all users")
  @APIResponses(value = {@APIResponse(responseCode = "200", description = "Success",
      content = @Content(schema = @Schema(implementation = UserResponse.class)))})
  public Response getAllUsers() {
    LOG.info("GET /api/users - Getting all users");
    List<UserResponse> users = userService.getAllUsers();
    return Response.ok(users).build();
  }

  /**
   * Get user by ID.
   *
   * @param id the user ID
   * @return the user
   */
  @GET
  @Path("/{id}")
  @RolesAllowed({"user", "admin"})
  @Operation(summary = "Get user by ID", description = "Returns a user by their ID")
  @APIResponses(value = {
      @APIResponse(responseCode = "200", description = "Success",
          content = @Content(schema = @Schema(implementation = UserResponse.class))),
      @APIResponse(responseCode = "404", description = "User not found")})
  public Response getUserById(
      @Parameter(description = "User ID", required = true) @PathParam("id") Long id) {
    LOG.infof("GET /api/users/%d - Getting user by id", id);
    UserResponse user = userService.getUserById(id);
    return Response.ok(user).build();
  }

  /**
   * Create a new user.
   *
   * @param request the user request
   * @return the created user
   */
  @POST
  @RolesAllowed("admin")
  @Operation(summary = "Create a new user", description = "Creates a new user")
  @APIResponses(value = {
      @APIResponse(responseCode = "201", description = "Created",
          content = @Content(schema = @Schema(implementation = UserResponse.class))),
      @APIResponse(responseCode = "400", description = "Invalid input")})
  public Response createUser(@Valid UserRequest request) {
    LOG.infof("POST /api/users - Creating user with email: %s", request.getEmail());
    UserResponse user = userService.createUser(request);
    return Response.status(Response.Status.CREATED).entity(user).build();
  }

  /**
   * Update an existing user.
   *
   * @param id the user ID
   * @param request the user request
   * @return the updated user
   */
  @PUT
  @Path("/{id}")
  @RolesAllowed("admin")
  @Operation(summary = "Update a user", description = "Updates an existing user")
  @APIResponses(value = {
      @APIResponse(responseCode = "200", description = "Success",
          content = @Content(schema = @Schema(implementation = UserResponse.class))),
      @APIResponse(responseCode = "400", description = "Invalid input"),
      @APIResponse(responseCode = "404", description = "User not found")})
  public Response updateUser(
      @Parameter(description = "User ID", required = true) @PathParam("id") Long id,
      @Valid UserRequest request) {
    LOG.infof("PUT /api/users/%d - Updating user", id);
    UserResponse user = userService.updateUser(id, request);
    return Response.ok(user).build();
  }

  /**
   * Delete a user.
   *
   * @param id the user ID
   * @return no content response
   */
  @DELETE
  @Path("/{id}")
  @RolesAllowed("admin")
  @Operation(summary = "Delete a user", description = "Deletes a user by their ID")
  @APIResponses(value = {@APIResponse(responseCode = "204", description = "Deleted successfully"),
      @APIResponse(responseCode = "404", description = "User not found")})
  public Response deleteUser(
      @Parameter(description = "User ID", required = true) @PathParam("id") Long id) {
    LOG.infof("DELETE /api/users/%d - Deleting user", id);
    userService.deleteUser(id);
    return Response.noContent().build();
  }
}
