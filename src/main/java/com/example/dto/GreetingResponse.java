package com.example.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for greeting responses.
 */
public class GreetingResponse implements Serializable {

  private String message;
  private LocalDateTime timestamp;

  public GreetingResponse() {
    this.timestamp = LocalDateTime.now();
  }

  public GreetingResponse(String message) {
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "GreetingResponse{message='" + message + "', timestamp=" + timestamp + "}";
  }
}
