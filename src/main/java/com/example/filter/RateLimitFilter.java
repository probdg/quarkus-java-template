package com.example.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.jboss.logging.Logger;

/**
 * Rate limiting filter that applies rate limits to API endpoints.
 */
@Provider
@Priority(1)
public class RateLimitFilter implements ContainerRequestFilter {

  private static final Logger LOG = Logger.getLogger(RateLimitFilter.class);

  // Rate limit: 100 requests per minute per IP
  private static final int RATE_LIMIT_CAPACITY = 100;
  private static final Duration RATE_LIMIT_REFILL_DURATION = Duration.ofMinutes(1);

  // DDoS detection thresholds
  private static final int DDOS_THRESHOLD = 200; // requests per minute
  private static final Duration DDOS_WINDOW = Duration.ofMinutes(1);

  // Store buckets per IP address
  private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

  // Track request counts for DDoS detection
  private final Map<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String ipAddress = getClientIpAddress(requestContext);
    String path = requestContext.getUriInfo().getPath();

    // Log activity
    LOG.infof("Activity: IP %s accessing %s %s", ipAddress, requestContext.getMethod(), path);

    // Get or create bucket for this IP
    Bucket bucket = buckets.computeIfAbsent(ipAddress, this::createBucket);

    // Check for DDoS patterns
    checkForDDoS(ipAddress, path);

    // Try to consume a token
    if (!bucket.tryConsume(1)) {
      // Rate limit exceeded
      LOG.errorf("Rate limit exceeded for IP: %s on path: %s", ipAddress, path);
      LOG.warnf("Possible DDoS attack detected from IP: %s - Rate limit exceeded", ipAddress);

      Response response = Response.status(Response.Status.TOO_MANY_REQUESTS)
          .entity("{\"error\": \"Rate limit exceeded. Please try again later.\"}").build();
      requestContext.abortWith(response);
    }
  }

  /**
   * Create a new bucket with rate limiting configuration.
   */
  private Bucket createBucket(String key) {
    Bandwidth limit = Bandwidth.classic(RATE_LIMIT_CAPACITY,
        Refill.intervally(RATE_LIMIT_CAPACITY, RATE_LIMIT_REFILL_DURATION));
    return Bucket.builder().addLimit(limit).build();
  }

  /**
   * Extract client IP address from request.
   */
  private String getClientIpAddress(ContainerRequestContext requestContext) {
    // Check X-Forwarded-For header first (for proxied requests)
    String forwardedFor = requestContext.getHeaderString("X-Forwarded-For");
    if (forwardedFor != null && !forwardedFor.isEmpty()) {
      return forwardedFor.split(",")[0].trim();
    }

    // Check X-Real-IP header
    String realIp = requestContext.getHeaderString("X-Real-IP");
    if (realIp != null && !realIp.isEmpty()) {
      return realIp;
    }

    // Fallback to a default value (in production, this would be from SecurityContext)
    return "unknown";
  }

  /**
   * Check for DDoS attack patterns.
   */
  private void checkForDDoS(String ipAddress, String path) {
    RequestCounter counter = requestCounters.computeIfAbsent(ipAddress, k -> new RequestCounter());

    counter.increment();

    // Check if threshold exceeded
    if (counter.getCount() > DDOS_THRESHOLD) {
      LOG.errorf("DDoS Attack Detected: IP %s has made %d requests in the last minute on path: %s",
          ipAddress, counter.getCount(), path);
    }

    // Reset counter if window expired
    if (counter.isExpired(DDOS_WINDOW)) {
      counter.reset();
    }
  }

  /**
   * Counter for tracking request rates.
   */
  private static class RequestCounter {
    private final AtomicInteger count = new AtomicInteger(0);
    private volatile long startTime = System.currentTimeMillis();

    public void increment() {
      count.incrementAndGet();
    }

    public int getCount() {
      return count.get();
    }

    public void reset() {
      count.set(0);
      startTime = System.currentTimeMillis();
    }

    public boolean isExpired(Duration window) {
      return System.currentTimeMillis() - startTime > window.toMillis();
    }
  }
}
