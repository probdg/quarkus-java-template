package com.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

/**
 * Service for greeting operations.
 */
@ApplicationScoped
public class GreetingService {

    private static final Logger LOG = Logger.getLogger(GreetingService.class);

    /**
     * Generates a greeting message for the given name.
     *
     * @param name the name to greet
     * @return the greeting message
     */
    public String greet(String name) {
        LOG.infof("Generating greeting for: %s", name);
        return String.format("Hello, %s! Welcome to Quarkus Template.", name);
    }

    /**
     * Generates a default greeting message.
     *
     * @return the default greeting message
     */
    public String getDefaultGreeting() {
        LOG.info("Generating default greeting");
        return "Hello! Welcome to Quarkus Template.";
    }
}
