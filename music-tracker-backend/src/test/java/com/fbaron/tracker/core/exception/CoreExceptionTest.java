package com.fbaron.tracker.core.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Plain JUnit tests for core domain exceptions (no Spring).
 */
class CoreExceptionTest {

    private static final String MESSAGE = "Test message";

    @Nested
    @DisplayName("TrackNotFoundException")
    class TrackNotFoundExceptionTest {

        @Test
        @DisplayName("has message and is RuntimeException")
        void messageAndType() {
            TrackNotFoundException ex = new TrackNotFoundException(MESSAGE);

            assertThat(ex).isInstanceOf(RuntimeException.class);
            assertThat(ex.getMessage()).isEqualTo(MESSAGE);
        }
    }

    @Nested
    @DisplayName("ProviderAuthenticationException")
    class ProviderAuthenticationExceptionTest {

        @Test
        @DisplayName("has message and is RuntimeException")
        void messageAndType() {
            ProviderAuthenticationException ex = new ProviderAuthenticationException(MESSAGE);

            assertThat(ex).isInstanceOf(RuntimeException.class);
            assertThat(ex.getMessage()).isEqualTo(MESSAGE);
        }
    }

    @Nested
    @DisplayName("ImageNotFoundException")
    class ImageNotFoundExceptionTest {

        @Test
        @DisplayName("has message and is RuntimeException")
        void messageAndType() {
            ImageNotFoundException ex = new ImageNotFoundException(MESSAGE);

            assertThat(ex).isInstanceOf(RuntimeException.class);
            assertThat(ex.getMessage()).isEqualTo(MESSAGE);
        }
    }

    @Nested
    @DisplayName("StorageException")
    class StorageExceptionTest {

        @Test
        @DisplayName("has message and is RuntimeException")
        void messageAndType() {
            StorageException ex = new StorageException(MESSAGE);

            assertThat(ex).isInstanceOf(RuntimeException.class);
            assertThat(ex.getMessage()).isEqualTo(MESSAGE);
        }
    }

    @Nested
    @DisplayName("FileReadException")
    class FileReadExceptionTest {

        @Test
        @DisplayName("has message and is RuntimeException")
        void messageAndType() {
            FileReadException ex = new FileReadException(MESSAGE);

            assertThat(ex).isInstanceOf(RuntimeException.class);
            assertThat(ex.getMessage()).isEqualTo(MESSAGE);
        }
    }
}
