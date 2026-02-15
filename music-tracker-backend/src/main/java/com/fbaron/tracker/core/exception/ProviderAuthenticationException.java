package com.fbaron.tracker.core.exception;

/**
 * Thrown when authentication with an external provider (e.g. Spotify) fails.
 */
public class ProviderAuthenticationException extends RuntimeException {

    public ProviderAuthenticationException(String message) {
        super(message);
    }

}
