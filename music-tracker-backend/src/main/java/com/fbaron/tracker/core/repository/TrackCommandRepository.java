package com.fbaron.tracker.core.repository;

import com.fbaron.tracker.core.model.Track;

public interface TrackCommandRepository {

    Track save(Track track);

}
