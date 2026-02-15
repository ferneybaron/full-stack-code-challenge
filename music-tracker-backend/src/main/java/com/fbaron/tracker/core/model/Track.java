package com.fbaron.tracker.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model for a music track.
 * Holds metadata (ISRC, name, artist, album, duration) and the local path to the cover image.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Track {

    /** International Standard Recording Code; unique identifier for the track. */
    private String isrCode;
    /** Track title. */
    private String name;
    /** Primary artist name. */
    private String artistName;
    /** Album name. */
    private String albumName;
    /** External provider's album ID (e.g. Spotify album ID). */
    private String albumId;
    /** Whether the track is marked as explicit. */
    private boolean isExplicit;
    /** Playback duration in seconds. */
    private Long playbackSeconds;
    /** Absolute path to the cover image file on disk. */
    private String coverPath;

}
