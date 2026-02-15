package com.fbaron.tracker.web.exception;

import com.fbaron.tracker.core.exception.FileReadException;
import com.fbaron.tracker.core.exception.ImageNotFoundException;
import com.fbaron.tracker.core.exception.ProviderAuthenticationException;
import com.fbaron.tracker.core.exception.StorageException;
import com.fbaron.tracker.core.exception.TrackNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


/**
 * Central exception handler: maps domain and framework exceptions to HTTP ProblemDetail responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Provider auth failure → 401 Unauthorized. */
    @ExceptionHandler(ProviderAuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleProviderAuthenticationException(ProviderAuthenticationException ex) {
        ProblemDetail problem = ProblemDetail
                .forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problem.setTitle("Authentication Error");
        problem.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    /** Music provider I/O (e.g. Spotify unreachable) → 503 Service Unavailable. */
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ProblemDetail> handleResourceAccessException(ResourceAccessException ex) {
        String detail = "The music provider is temporarily unreachable. Please try again later.";
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                detail
        );
        problem.setTitle("Music Provider Unavailable");
        problem.setProperty("timestamp", LocalDateTime.now());
        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
            problem.setProperty("cause", ex.getCause().getMessage());
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(problem);
    }

    /** File read error (e.g. cover image) → 500 Internal Server Error. */
    @ExceptionHandler(FileReadException.class)
    public ResponseEntity<ProblemDetail> handleStorageError(FileReadException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "The server encountered a problem reading the requested file."
        );
        problem.setTitle("Storage Retrieval Error");
        problem.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    /** Storage write error → 503 Service Unavailable. */
    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ProblemDetail> handleStorageException(StorageException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "The server encountered a problem saving the requested file."
        );
        problem.setTitle("Storage Error");
        problem.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(problem);
    }

    /** Cover image not found (e.g. album has no images) → 404 Not Found. */
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleImageNotFound(ImageNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problem.setTitle("Image Not Found");
        problem.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    /** Track not found → 404 Not Found. */
    @ExceptionHandler(TrackNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(TrackNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problem.setTitle("Track Not Found");
        problem.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    /** Validation errors (e.g. invalid ISRC) → 400 Bad Request with field messages. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problem.setTitle("Validation Failed");
        problem.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

}
