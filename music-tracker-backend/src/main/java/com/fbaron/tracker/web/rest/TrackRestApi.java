package com.fbaron.tracker.web.rest;

import com.fbaron.tracker.web.dto.RegisterTrackDto;
import com.fbaron.tracker.web.dto.TrackDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

/**
 * REST API contract for track management: register by ISRC, get metadata, get cover image.
 * OpenAPI annotations describe operations and responses.
 */
@Tag(name = "Track Management",
        description = "Endpoints for Music Provider track metadata integration and storage")
public interface TrackRestApi {

    @Operation(summary = "Create and store track",
            description = "Fetches metadata from Music provider and stores it locally. Returns 201 if created," +
                    " 200 if it already exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Track created successfully"),
            @ApiResponse(responseCode = "200", description = "Track already exists, returning stored data"),
            @ApiResponse(responseCode = "404", description = "Track not found in streaming provider",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "503", description = "Music provider API or Storage unavailable",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    ResponseEntity<TrackDto> registerTrack(RegisterTrackDto dto);

    @Operation(summary = "Get track metadata", description = "Retrieves stored track information from the local database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metadata retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Track not found in local database",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<TrackDto> getTrackMetadata(
            @Parameter(description = "The ISRC code of the track", example = "USMC18620549")
            String isrCode);

    @Operation(summary = "Get cover image", description = "Returns the binary image data for the track cover.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully",
                    content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "404", description = "Track not found in local database",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Error reading file from disk",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<byte[]> getCover(
            @Parameter(description = "The ISRC code of the track", example = "USMC18620549")
            String isrCode);

}
