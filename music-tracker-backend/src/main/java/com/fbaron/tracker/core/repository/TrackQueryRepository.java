package com.fbaron.tracker.core.repository;

import com.fbaron.tracker.core.model.Track;

import java.util.Optional;

/**
 * Port for querying persisted tracks (read-only).
 */
public interface TrackQueryRepository {

    /**
     * Looks up a track by ISRC.
     *
     * @param isrCode International Standard Recording Code
     * @return the track if found, otherwise empty
     */
    Optional<Track> findByIsrCode(String isrCode);

}
