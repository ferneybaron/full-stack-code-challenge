package com.fbaron.tracker.core.exception;

/**
 * Thrown when reading a file from disk fails (e.g. cover image not found or I/O error).
 */
public class FileReadException extends RuntimeException {

    public FileReadException(String message) {
        super(message);
    }

}