package com.fbaron.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Music Tracker Backend application.
 * Bootstraps Spring Boot and enables component scanning and autoconfiguration.
 */
@SpringBootApplication
public class Application {

    /**
     * Starts the application.
     *
     * @param args command-line arguments (optional)
     */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
