package com.fbaron.tracker.web.exception;

import com.fbaron.tracker.core.exception.FileReadException;
import com.fbaron.tracker.core.exception.ImageNotFoundException;
import com.fbaron.tracker.core.exception.ProviderAuthenticationException;
import com.fbaron.tracker.core.exception.StorageException;
import com.fbaron.tracker.core.exception.TrackNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Plain JUnit tests for GlobalExceptionHandler (no Spring context).
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Nested
    @DisplayName("handleProviderAuthenticationException")
    class ProviderAuthentication {

        @Test
        @DisplayName("returns 401 with title and message")
        void returns401() {
            ProviderAuthenticationException ex = new ProviderAuthenticationException("Invalid token");

            ResponseEntity<ProblemDetail> response =
                    handler.handleProviderAuthenticationException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            ProblemDetail body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.getTitle()).isEqualTo("Authentication Error");
            assertThat(body.getDetail()).isEqualTo("Invalid token");
            assertThat(body.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(body.getProperties()).containsKey("timestamp");
        }
    }

    @Nested
    @DisplayName("handleResourceAccessException")
    class ResourceAccess {

        @Test
        @DisplayName("returns 503 with fixed detail and optional cause")
        void returns503() {
            ResourceAccessException ex = new ResourceAccessException("Connection refused",
                    new ConnectException("Connection refused"));

            ResponseEntity<ProblemDetail> response =
                    handler.handleResourceAccessException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            ProblemDetail body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.getTitle()).isEqualTo("Music Provider Unavailable");
            assertThat(body.getDetail()).isEqualTo("The music provider is temporarily unreachable. Please try again later.");
            assertThat(body.getProperties()).containsKey("timestamp");
            assertThat(body.getProperties()).containsKey("cause");
            assertThat(body.getProperties().get("cause")).isEqualTo("Connection refused");
        }

        @Test
        @DisplayName("returns 503 without cause when getCause is null")
        void returns503WithoutCause() {
            ResourceAccessException ex = new ResourceAccessException("I/O error");

            ResponseEntity<ProblemDetail> response =
                    handler.handleResourceAccessException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            ProblemDetail body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.getProperties()).doesNotContainKey("cause");
        }
    }

    @Nested
    @DisplayName("handleStorageError (FileReadException)")
    class FileRead {

        @Test
        @DisplayName("returns 500 with fixed detail")
        void returns500() {
            FileReadException ex = new FileReadException("Read failed");

            ResponseEntity<ProblemDetail> response =
                    handler.handleStorageError(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            ProblemDetail body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.getTitle()).isEqualTo("Storage Retrieval Error");
            assertThat(body.getDetail()).isEqualTo("The server encountered a problem reading the requested file.");
            assertThat(body.getProperties()).containsKey("timestamp");
        }
    }

    @Nested
    @DisplayName("handleStorageException")
    class Storage {

        @Test
        @DisplayName("returns 503 with fixed detail")
        void returns503() {
            StorageException ex = new StorageException("Write failed");

            ResponseEntity<ProblemDetail> response =
                    handler.handleStorageException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            ProblemDetail body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.getTitle()).isEqualTo("Storage Error");
            assertThat(body.getDetail()).isEqualTo("The server encountered a problem saving the requested file.");
            assertThat(body.getProperties()).containsKey("timestamp");
        }
    }

    @Nested
    @DisplayName("handleImageNotFound")
    class ImageNotFound {

        @Test
        @DisplayName("returns 404 with exception message")
        void returns404() {
            ImageNotFoundException ex = new ImageNotFoundException("No images for album xyz");

            ResponseEntity<ProblemDetail> response =
                    handler.handleImageNotFound(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            ProblemDetail body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.getTitle()).isEqualTo("Image Not Found");
            assertThat(body.getDetail()).isEqualTo("No images for album xyz");
            assertThat(body.getProperties()).containsKey("timestamp");
        }
    }

    @Nested
    @DisplayName("handleNotFound (TrackNotFoundException)")
    class TrackNotFound {

        @Test
        @DisplayName("returns 404 with exception message")
        void returns404() {
            TrackNotFoundException ex = new TrackNotFoundException("Track not found with ISRC: ABC");

            ResponseEntity<ProblemDetail> response =
                    handler.handleNotFound(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            ProblemDetail body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.getTitle()).isEqualTo("Track Not Found");
            assertThat(body.getDetail()).isEqualTo("Track not found with ISRC: ABC");
            assertThat(body.getProperties()).containsKey("timestamp");
        }
    }

    @Nested
    @DisplayName("handleValidation (MethodArgumentNotValidException)")
    class Validation {

        @Test
        @DisplayName("returns 400 with field errors in detail")
        void returns400() {
            MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            when(ex.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getFieldErrors()).thenReturn(List.of(
                    new FieldError("registerTrackDto", "isrCode", "must not be blank"),
                    new FieldError("registerTrackDto", "isrCode", "size must be between 12 and 20")
            ));

            ResponseEntity<ProblemDetail> response =
                    handler.handleValidation(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            ProblemDetail body = response.getBody();
            assertThat(body).isNotNull();
            assertThat(body.getTitle()).isEqualTo("Validation Failed");
            assertThat(body.getDetail())
                    .contains("isrCode")
                    .contains("must not be blank")
                    .contains("size must be between 12 and 20");
            assertThat(body.getProperties()).containsKey("timestamp");
        }
    }
}
