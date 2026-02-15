package com.fbaron.tracker.core.exception;

/**
 * Thrown when a cover image cannot be found (e.g. album has no images in the provider).
 */
public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(String message) {
        super(message);
    }

}
