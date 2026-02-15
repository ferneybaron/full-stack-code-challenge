package com.fbaron.tracker.core.exception;

/**
 * Thrown when a storage operation fails (e.g. writing a cover image to disk).
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

}