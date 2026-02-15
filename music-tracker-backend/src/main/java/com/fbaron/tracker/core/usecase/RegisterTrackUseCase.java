package com.fbaron.tracker.core.usecase;

import com.fbaron.tracker.core.model.RegistrationResult;

/**
 * Use case: register a track by ISRC (fetch from provider, store cover, persist).
 */
public interface RegisterTrackUseCase {

    /**
     * Registers a track by ISRC. If already present, returns existing track without re-saving.
     *
     * @param isrCode International Standard Recording Code
     * @return result with track and whether it was newly saved
     */
    RegistrationResult register(String isrCode);

}
