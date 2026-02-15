package com.fbaron.tracker.core.exception;

/**
 * Thrown when a track cannot be found (e.g. by ISRC in the provider or in local storage).
 */
public class TrackNotFoundException extends RuntimeException {

    public TrackNotFoundException(String message) {
        super(message);
    }

}
