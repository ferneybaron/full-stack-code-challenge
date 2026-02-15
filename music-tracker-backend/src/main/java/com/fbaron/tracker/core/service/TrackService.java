package com.fbaron.tracker.core.service;

import com.fbaron.tracker.core.exception.TrackNotFoundException;
import com.fbaron.tracker.core.model.RegistrationResult;
import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.core.repository.FileStorageRepository;
import com.fbaron.tracker.core.repository.MusicProviderRepository;
import com.fbaron.tracker.core.repository.TrackCommandRepository;
import com.fbaron.tracker.core.repository.TrackQueryRepository;
import com.fbaron.tracker.core.usecase.GetCoverImageUseCase;
import com.fbaron.tracker.core.usecase.GetTrackUseCase;
import com.fbaron.tracker.core.usecase.RegisterTrackUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * Application service implementing track registration, retrieval, and cover image delivery.
 * Orchestrates the music provider, persistence, and file storage ports.
 */
@Slf4j
@RequiredArgsConstructor
public class TrackService implements RegisterTrackUseCase, GetTrackUseCase, GetCoverImageUseCase {

    private final TrackQueryRepository trackQueryRepository;
    private final MusicProviderRepository musicProviderRepository;
    private final FileStorageRepository fileStorageRepository;
    private final TrackCommandRepository trackCommandRepository;

    /**
     * Registers a track by ISRC: checks if already stored; if not, fetches metadata and cover
     * from the provider, saves the cover to disk, and persists the track.
     */
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
        String imagePath = fileStorageRepository.saveToDisk(isrCode, imageBytes);

        // 4. updated the object with the image localization
        track.setCoverPath(imagePath);

        // Persist the track to DB and return the RegistrationResult
        track = trackCommandRepository.save(track);
        log.info("Track registered successfully: isrCode={}, name={}", isrCode, track.getName());
        return new RegistrationResult(track, true);
    }

    /**
     * Returns the stored track for the given ISRC.
     */
    @Override
    public Track getTrackByIsrCode(String isrCode) {
        return trackQueryRepository.findByIsrCode(isrCode)
                .orElseThrow(() -> {
                    log.error("Track not found: isrCode={}", isrCode);
                    return new TrackNotFoundException("Track not found with ISRC: " + isrCode);
                });
    }

    /**
     * Returns the cover image bytes for the track with the given ISRC (read from local storage).
     */
    @Override
    public byte[] getCoverImage(String isrCode) {
        Track track = trackQueryRepository.findByIsrCode(isrCode)
                .orElseThrow(() -> {
                    log.error("Track not found for cover: isrCode={}", isrCode);
                    return new TrackNotFoundException("Track not found with ISRC: " + isrCode);
                });

        return fileStorageRepository.readFromDisk(track.getCoverPath());
    }

}
