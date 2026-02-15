package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Data Transfer Object for user requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequest implements Serializable {

  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Email must be valid")
  private String email;

  @NotBlank(message = "Name cannot be blank")
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
  private String name;
}
