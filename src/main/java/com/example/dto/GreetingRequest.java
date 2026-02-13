package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Data Transfer Object for greeting requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GreetingRequest implements Serializable {

  @NotBlank(message = "Name cannot be blank")
  @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
  private String name;
}
