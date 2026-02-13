package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for greeting responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GreetingResponse implements Serializable {

  private String message;
  private LocalDateTime timestamp;

  public GreetingResponse(String message) {
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }
}
