package com.fbaron.tracker.web.dto;

/**
 * API response for track metadata (ISRC, name, artist, album, duration, cover path, etc.).
 */
public record TrackDto(
        String isrCode,
        String name,
        String artistName,
        String albumName,
        String albumId,
        boolean isExplicit,
        Long playbackSeconds,
        String coverPath) {
}
