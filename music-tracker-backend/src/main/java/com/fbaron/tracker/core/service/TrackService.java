package com.fbaron.tracker.core.service;

import com.fbaron.tracker.core.model.RegistrationResult;
import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.core.repository.MusicProviderRepository;
import com.fbaron.tracker.core.repository.TrackQueryRepository;
import com.fbaron.tracker.core.usecase.RegisterTrackUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;


@RequiredArgsConstructor
public class TrackService implements RegisterTrackUseCase {

    private static final Logger log = LogManager.getLogger(TrackService.class);

    private final TrackQueryRepository trackQueryRepository;
    private final MusicProviderRepository musicProviderRepository;

    @Override
    public RegistrationResult register(String isrCode) {
        // 1. check if it already exists //No need to care about updating an already existing ISRC,
        // skipping or giving back an error is enough.
        Optional<Track> existingTrack = trackQueryRepository.findByIsrCode(isrCode);

        if (existingTrack.isPresent()) {
            log.info("Track already registered, returning existing: isrCode = {}", isrCode);
            return new RegistrationResult(existingTrack.get(), false);
        }

        log.info("Registering new Track: isrCode = {}", isrCode);

        // 2. Fetch from Spotify
        Track track = musicProviderRepository.fetchMetadata(isrCode);

        // 3. Store the image
        byte[] imageBytes = musicProviderRepository.fetchCoverImage(track.getAlbumId());

        // 4. updated the object with the image localization

        // Persist the track to DB and return the RegistrationResult
        return null;
    }

}
