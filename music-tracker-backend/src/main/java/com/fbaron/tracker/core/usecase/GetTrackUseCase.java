package com.fbaron.tracker.core.usecase;

import com.fbaron.tracker.core.exception.TrackNotFoundException;
import com.fbaron.tracker.core.model.Track;

/**
 * Use case: retrieve stored track metadata by ISRC.
 */
public interface GetTrackUseCase {

    /**
     * Returns the track for the given ISRC.
     *
     * @param isrCode International Standard Recording Code
     * @return the track
     * @throws TrackNotFoundException if not found
     */
    Track getTrackByIsrCode(String isrCode);

}
