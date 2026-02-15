package com.fbaron.tracker.core.repository;

import com.fbaron.tracker.core.model.Track;

/**
 * Port for persisting tracks (write operations).
 */
public interface TrackCommandRepository {

    /**
     * Saves a track (insert or update).
     *
     * @param track the track to save
     * @return the saved track (possibly with generated/updated fields)
     */
    Track save(Track track);

}
