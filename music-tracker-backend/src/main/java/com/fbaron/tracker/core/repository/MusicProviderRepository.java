package com.fbaron.tracker.core.repository;

import com.fbaron.tracker.core.exception.ImageNotFoundException;
import com.fbaron.tracker.core.exception.ProviderAuthenticationException;
import com.fbaron.tracker.core.exception.TrackNotFoundException;
import com.fbaron.tracker.core.model.Track;

/**
 * Port for fetching track metadata and cover images from an external music provider (e.g. Spotify).
 */
public interface MusicProviderRepository {

    /**
     * Fetches track metadata by ISRC.
     *
     * @param isrCode International Standard Recording Code
     * @return track with metadata (name, artist, album, duration, etc.)
     * @throws TrackNotFoundException if no track is found for the ISRC
     * @throws ProviderAuthenticationException if provider auth fails
     */
    Track fetchMetadata(String isrCode);

    /**
     * Fetches the cover image bytes for an album.
     *
     * @param albumId provider's album ID (e.g. Spotify album ID)
     * @return raw image bytes (e.g. JPEG)
     * @throws ImageNotFoundException if the album has no images
     * @throws ProviderAuthenticationException if provider auth fails
     */
    byte[] fetchCoverImage(String albumId);

}
