package com.fbaron.tracker.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request body for registering a track (ISRC only; validated).
 */
public record RegisterTrackDto(
        @NotBlank(message = "ISRC code is required")
        @Size(min = 12, max = 20)
        @Pattern(regexp = "^[A-Z0-9\\-]+$", message = "ISRC must be alphanumeric (optionally with hyphens")
        @Schema(description = "The ISRC code of the track",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "USMC18620549")
        String isrCode) {
}
