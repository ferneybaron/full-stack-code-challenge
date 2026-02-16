package com.fbaron.tracker.core.service;

import com.fbaron.tracker.core.exception.TrackNotFoundException;
import com.fbaron.tracker.core.model.RegistrationResult;
import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.core.repository.FileStorageRepository;
import com.fbaron.tracker.core.repository.MusicProviderRepository;
import com.fbaron.tracker.core.repository.TrackCommandRepository;
import com.fbaron.tracker.core.repository.TrackQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Unit tests for {@link TrackService}: register, getTrackByIsrCode, getCoverImage with mocked repositories. */
@ExtendWith(MockitoExtension.class)
class TrackServiceTest {

    private static final String ISRC = "USMC18620549";
    private static final String COVER_PATH = "/storage/USMC18620549.jpg";

    @Mock
    private MusicProviderRepository musicProviderRepository;
    @Mock
    private TrackQueryRepository trackQueryRepository;
    @Mock
    private TrackCommandRepository trackCommandRepository;
    @Mock
    private FileStorageRepository fileStorageRepository;

    private TrackService trackService;

    @BeforeEach
    void setUp() {
        trackService = new TrackService(
                trackQueryRepository,
                musicProviderRepository,
                fileStorageRepository,
                trackCommandRepository);
    }

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("returns existing track with saved=false when track already exists")
        void returnsExistingTrackWhenAlreadyExists() {
            Track existing = track(ISRC);
            when(trackQueryRepository.findByIsrCode(ISRC)).thenReturn(Optional.of(existing));

            RegistrationResult result = trackService.register(ISRC);

            assertThat(result.saved()).isFalse();
            assertThat(result.track().getIsrCode()).isEqualTo(ISRC);
            verify(trackQueryRepository).findByIsrCode(ISRC);
            verify(musicProviderRepository, never()).fetchMetadata(any());
        }

        @Test
        @DisplayName("fetches from provider, saves file and track, returns saved=true when new")
        void createsNewTrackAndReturnsSavedTrue() {
            when(trackQueryRepository.findByIsrCode(ISRC)).thenReturn(Optional.empty());
            Track fromProvider = track(ISRC);
            fromProvider.setCoverPath(null);
            when(musicProviderRepository.fetchMetadata(ISRC)).thenReturn(fromProvider);
            when(musicProviderRepository.fetchCoverImage(fromProvider.getAlbumId())).thenReturn(new byte[]{1, 2, 3});
            when(fileStorageRepository.saveToDisk(eq(ISRC), any(byte[].class))).thenReturn(COVER_PATH);
            Track savedTrack = track(ISRC);
            savedTrack.setCoverPath(COVER_PATH);
            when(trackCommandRepository.save(any(Track.class))).thenReturn(savedTrack);

            RegistrationResult result = trackService.register(ISRC);

            assertThat(result.saved()).isTrue();
            assertThat(result.track().getCoverPath()).isEqualTo(COVER_PATH);
            verify(fileStorageRepository).saveToDisk(eq(ISRC), any(byte[].class));
            verify(trackCommandRepository).save(any(Track.class));
        }
    }

    @Nested
    @DisplayName("getTrackByIsrCode")
    class GetTrackByIsrCode {

        @Test
        @DisplayName("returns track when found")
        void returnsTrackWhenFound() {
            Track expected = track(ISRC);
            when(trackQueryRepository.findByIsrCode(ISRC)).thenReturn(Optional.of(expected));

            Track result = trackService.getTrackByIsrCode(ISRC);

            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("throws TrackNotFoundException when not found")
        void throwsWhenNotFound() {
            when(trackQueryRepository.findByIsrCode(ISRC)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> trackService.getTrackByIsrCode(ISRC))
                    .isInstanceOf(TrackNotFoundException.class)
                    .hasMessageContaining(ISRC);
        }
    }

    @Nested
    @DisplayName("getCoverImage")
    class GetCoverImage {

        @Test
        @DisplayName("returns image bytes when track exists")
        void returnsImageWhenTrackExists() {
            Track track = track(ISRC);
            track.setCoverPath(COVER_PATH);
            when(trackQueryRepository.findByIsrCode(ISRC)).thenReturn(Optional.of(track));
            byte[] imageBytes = new byte[]{1, 2, 3};
            when(fileStorageRepository.readFromDisk(COVER_PATH)).thenReturn(imageBytes);

            byte[] result = trackService.getCoverImage(ISRC);

            assertThat(result).isEqualTo(imageBytes);
            verify(fileStorageRepository).readFromDisk(COVER_PATH);
        }

        @Test
        @DisplayName("throws TrackNotFoundException when track not found")
        void throwsWhenTrackNotFound() {
            when(trackQueryRepository.findByIsrCode(ISRC)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> trackService.getCoverImage(ISRC))
                    .isInstanceOf(TrackNotFoundException.class);
        }
    }

    private static Track track(String isrCode) {
        return Track.builder()
                .isrCode(isrCode)
                .name("Track Name")
                .artistName("Artist")
                .albumName("Album")
                .albumId("album-123")
                .isExplicit(false)
                .playbackSeconds(180L)
                .coverPath(COVER_PATH)
                .build();
    }
}
