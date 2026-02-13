package com.example.service;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * Service for Redis cache operations.
 */
@ApplicationScoped
@Slf4j
public class RedisService {

  @Inject
  RedisDataSource redisDataSource;

  private ValueCommands<String, String> commands;

  public void init() {
    commands = redisDataSource.value(String.class);
  }

  /**
   * Store a value in Redis with expiration.
   */
  public void set(String key, String value, Duration ttl) {
    if (commands == null) {
      init();
    }
    commands.setex(key, ttl.getSeconds(), value);
    log.debug("Stored key: {} with TTL: {}s", key, ttl.getSeconds());
  }

  /**
   * Retrieve a value from Redis.
   */
  public String get(String key) {
    if (commands == null) {
      init();
    }
    String value = commands.get(key);
    log.debug("Retrieved key: {} with value: {}", key, value);
    return value;
  }

  /**
   * Delete a key from Redis.
   */
  public void delete(String key) {
    if (commands == null) {
      init();
    }
    commands.getdel(key);
    log.debug("Deleted key: {}", key);
  }

  /**
   * Check if a key exists.
   */
  public boolean exists(String key) {
    if (commands == null) {
      init();
    }
    String value = commands.get(key);
    return value != null;
  }
}
