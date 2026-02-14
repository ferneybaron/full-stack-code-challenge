package com.fbaron.tracker.core.repository;

import com.fbaron.tracker.core.model.Track;

import java.util.Optional;

public interface TrackQueryRepository {

    Optional<Track> findByIsrCode(String isrCode);

}
