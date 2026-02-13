package com.example.service;

import com.example.dto.UserRequest;
import com.example.dto.UserResponse;
import com.example.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for user operations.
 */
@ApplicationScoped
public class UserService {

  private static final Logger LOG = Logger.getLogger(UserService.class);

  /**
   * Get all users.
   *
   * @return list of user responses
   */
  public List<UserResponse> getAllUsers() {
    LOG.info("Getting all users");
    return User.<User>listAll().stream().map(this::toResponse).collect(Collectors.toList());
  }

  /**
   * Get user by ID.
   *
   * @param id the user ID
   * @return the user response
   * @throws NotFoundException if user not found
   */
  public UserResponse getUserById(Long id) {
    LOG.infof("Getting user by id: %d", id);
    User user = User.findById(id);
    if (user == null) {
      throw new NotFoundException("User not found with id: " + id);
    }
    return toResponse(user);
  }

  /**
   * Create a new user.
   *
   * @param request the user request
   * @return the created user response
   */
  @Transactional
  public UserResponse createUser(UserRequest request) {
    LOG.infof("Creating user with email: %s", request.getEmail());

    // Check if user with email already exists
    User existingUser = User.find("email", request.getEmail()).firstResult();
    if (existingUser != null) {
      throw new IllegalArgumentException("User with email already exists: " + request.getEmail());
    }

    User user = new User();
    user.setEmail(request.getEmail());
    user.setName(request.getName());
    user.persist();

    LOG.infof("User created with id: %d", user.id);
    return toResponse(user);
  }

  /**
   * Update an existing user.
   *
   * @param id the user ID
   * @param request the user request
   * @return the updated user response
   * @throws NotFoundException if user not found
   */
  @Transactional
  public UserResponse updateUser(Long id, UserRequest request) {
    LOG.infof("Updating user with id: %d", id);
    User user = User.findById(id);
    if (user == null) {
      throw new NotFoundException("User not found with id: " + id);
    }

    // Check if email is being changed to an existing email
    if (!user.getEmail().equals(request.getEmail())) {
      User existingUser = User.find("email", request.getEmail()).firstResult();
      if (existingUser != null) {
        throw new IllegalArgumentException("User with email already exists: " + request.getEmail());
      }
    }

    user.setEmail(request.getEmail());
    user.setName(request.getName());
    user.persist();

    LOG.infof("User updated with id: %d", user.id);
    return toResponse(user);
  }

  /**
   * Delete a user.
   *
   * @param id the user ID
   * @throws NotFoundException if user not found
   */
  @Transactional
  public void deleteUser(Long id) {
    LOG.infof("Deleting user with id: %d", id);
    User user = User.findById(id);
    if (user == null) {
      throw new NotFoundException("User not found with id: " + id);
    }
    user.delete();
    LOG.infof("User deleted with id: %d", id);
  }

  /**
   * Convert User entity to UserResponse DTO.
   *
   * @param user the user entity
   * @return the user response DTO
   */
  private UserResponse toResponse(User user) {
    return new UserResponse(user.id, user.getEmail(), user.getName(), user.getCreatedAt(),
        user.getUpdatedAt());
  }
}
