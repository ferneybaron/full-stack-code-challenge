package com.fbaron.tracker.core.model;

/**
 * Result of a track registration operation.
 *
 * @param track  the track (either newly created or already existing)
 * @param saved  true if the track was newly saved, false if it already existed
 */
public record RegistrationResult(Track track, Boolean saved) {
}
