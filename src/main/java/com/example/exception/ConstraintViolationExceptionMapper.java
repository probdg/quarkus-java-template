package com.example.exception;

import com.example.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.stream.Collectors;

/**
 * Exception mapper for constraint violations.
 */
@Provider
public class ConstraintViolationExceptionMapper
    implements ExceptionMapper<ConstraintViolationException> {

  private static final Logger LOG = Logger.getLogger(ConstraintViolationExceptionMapper.class);

  @Context
  UriInfo uriInfo;

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    String path = uriInfo != null ? uriInfo.getPath() : "unknown";

    String message = exception.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));

    LOG.errorf("Validation error at %s: %s", path, message);

    ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(),
        "Validation Error", message, path);

    return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
  }
}
