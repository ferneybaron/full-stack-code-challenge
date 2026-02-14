package com.fbaron.tracker.core.repository;

import com.fbaron.tracker.core.model.Track;

public interface MusicProviderRepository {

    Track fetchMetadata(String isrCode);

    byte[] fetchCoverImage(String albumId);

}
