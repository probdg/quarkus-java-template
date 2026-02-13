package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for error responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse implements Serializable {

  private int status;
  private String error;
  private String message;
  private String path;
  private LocalDateTime timestamp;

  public ErrorResponse(int status, String error, String message, String path) {
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
    this.timestamp = LocalDateTime.now();
  }
}
