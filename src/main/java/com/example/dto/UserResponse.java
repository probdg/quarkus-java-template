package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for user responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponse implements Serializable {

  private Long id;
  private String email;
  private String name;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
