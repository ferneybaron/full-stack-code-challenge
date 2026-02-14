package com.fbaron.tracker.web.dto;

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
