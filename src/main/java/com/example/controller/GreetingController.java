package com.example.controller;

import com.example.dto.GreetingRequest;
import com.example.dto.GreetingResponse;
import com.example.service.GreetingService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

/**
 * REST controller for greeting operations.
 */
@Path("/api/greeting")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Greeting", description = "Greeting operations")
public class GreetingController {

  private static final Logger LOG = Logger.getLogger(GreetingController.class);

  @Inject
  GreetingService greetingService;

  /**
   * Get a default greeting.
   *
   * @return the greeting response
   */
  @GET
  @Operation(summary = "Get default greeting", description = "Returns a default greeting message")
  @APIResponses(value = {@APIResponse(responseCode = "200", description = "Success",
      content = @Content(schema = @Schema(implementation = GreetingResponse.class)))})
  public Response getGreeting() {
    LOG.info("GET /api/greeting - Getting default greeting");
    String message = greetingService.getDefaultGreeting();
    GreetingResponse response = new GreetingResponse(message);
    return Response.ok(response).build();
  }

  /**
   * Get a personalized greeting.
   *
   * @param name the name to greet
   * @return the greeting response
   */
  @GET
  @Path("/{name}")
  @Operation(summary = "Get personalized greeting",
      description = "Returns a personalized greeting message")
  @APIResponses(value = {
      @APIResponse(responseCode = "200", description = "Success",
          content = @Content(schema = @Schema(implementation = GreetingResponse.class))),
      @APIResponse(responseCode = "400", description = "Invalid input")})
  public Response getGreetingByName(@PathParam("name") String name) {
    LOG.infof("GET /api/greeting/%s - Getting personalized greeting", name);

    if (name == null || name.isBlank()) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(new GreetingResponse("Name cannot be empty")).build();
    }

    String message = greetingService.greet(name);
    GreetingResponse response = new GreetingResponse(message);
    return Response.ok(response).build();
  }

  /**
   * Create a personalized greeting.
   *
   * @param request the greeting request
   * @return the greeting response
   */
  @POST
  @Operation(summary = "Create personalized greeting",
      description = "Creates a personalized greeting message")
  @APIResponses(value = {
      @APIResponse(responseCode = "201", description = "Created",
          content = @Content(schema = @Schema(implementation = GreetingResponse.class))),
      @APIResponse(responseCode = "400", description = "Invalid input")})
  public Response createGreeting(@Valid GreetingRequest request) {
    LOG.infof("POST /api/greeting - Creating greeting for: %s", request.getName());
    String message = greetingService.greet(request.getName());
    GreetingResponse response = new GreetingResponse(message);
    return Response.status(Response.Status.CREATED).entity(response).build();
  }
}
