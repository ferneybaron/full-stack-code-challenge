package com.fbaron.tracker.core.usecase;

import com.fbaron.tracker.core.exception.FileReadException;
import com.fbaron.tracker.core.exception.TrackNotFoundException;

/**
 * Use case: retrieve cover image bytes for a track by ISRC (from local storage).
 */
public interface GetCoverImageUseCase {

    /**
     * Returns the cover image bytes for the track with the given ISRC.
     *
     * @param isrCode International Standard Recording Code
     * @return raw image bytes (e.g. JPEG)
     * @throws TrackNotFoundException if track not found
     * @throws FileReadException if reading the file fails
     */
    byte[] getCoverImage(String isrCode);

}
