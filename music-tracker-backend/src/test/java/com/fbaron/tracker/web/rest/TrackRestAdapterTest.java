package com.fbaron.tracker.web.rest;

import com.fbaron.tracker.core.exception.TrackNotFoundException;
import com.fbaron.tracker.core.model.RegistrationResult;
import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.core.usecase.GetCoverImageUseCase;
import com.fbaron.tracker.core.usecase.GetTrackUseCase;
import com.fbaron.tracker.core.usecase.RegisterTrackUseCase;
import com.fbaron.tracker.web.config.SecurityConfig;
import com.fbaron.tracker.web.dto.TrackDto;
import com.fbaron.tracker.web.mapper.TrackDtoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Slice tests for {@link TrackRestAdapter}: REST endpoints with mocked use cases and HTTP Basic auth. */
@WebMvcTest(TrackRestAdapter.class)
@Import(SecurityConfig.class)
class TrackRestAdapterTest {

    private static final String ISRC = "USMC18620549";
    private static final String TRACK_JSON = "{\"isrCode\":\"USMC18620549\"}";
    private static final String TEST_USER = "testuser";
    private static final String TEST_PASSWORD = "testpass";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterTrackUseCase registerTrackUseCase;
    @MockitoBean
    private GetTrackUseCase getTrackUseCase;
    @MockitoBean
    private GetCoverImageUseCase getCoverImageUseCase;
    @MockitoBean
    private TrackDtoMapper trackDtoMapper;

    @Test
    @DisplayName("returns 401 when request has no credentials")
    void returns401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/tracks/{isrCode}", ISRC)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Nested
    @DisplayName("GET /api/v1/tracks/{isrCode}")
    class GetTrackMetadata {

        @Test
        @DisplayName("returns 200 and track when found")
        void returns200WhenFound() throws Exception {
            Track track = track(ISRC);
            TrackDto dto = toDto(track);
            when(getTrackUseCase.getTrackByIsrCode(ISRC)).thenReturn(track);
            when(trackDtoMapper.toDto(track)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/tracks/{isrCode}", ISRC)
                            .with(httpBasic(TEST_USER, TEST_PASSWORD))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("returns 404 when track not found")
        void returns404WhenNotFound() throws Exception {
            when(getTrackUseCase.getTrackByIsrCode(ISRC))
                    .thenThrow(new TrackNotFoundException("Track not found with ISRC: " + ISRC));

            mockMvc.perform(get("/api/v1/tracks/{isrCode}", ISRC)
                            .with(httpBasic(TEST_USER, TEST_PASSWORD))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/tracks")
    class RegisterTrack {

        @Test
        @DisplayName("returns 201 when track is created")
        void returns201WhenCreated() throws Exception {
            Track track = track(ISRC);
            TrackDto dto = toDto(track);
            when(registerTrackUseCase.register(ISRC)).thenReturn(new RegistrationResult(track, true));
            when(trackDtoMapper.toDto(any())).thenReturn(dto);

            mockMvc.perform(post("/api/v1/tracks")
                            .with(httpBasic(TEST_USER, TEST_PASSWORD))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TRACK_JSON))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("returns 200 when track already exists")
        void returns200WhenAlreadyExists() throws Exception {
            Track track = track(ISRC);
            TrackDto dto = toDto(track);
            when(registerTrackUseCase.register(ISRC)).thenReturn(new RegistrationResult(track, false));
            when(trackDtoMapper.toDto(any())).thenReturn(dto);

            mockMvc.perform(post("/api/v1/tracks")
                            .with(httpBasic(TEST_USER, TEST_PASSWORD))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TRACK_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("returns 400 when isrCode is blank")
        void returns400WhenValidationFails() throws Exception {
            mockMvc.perform(post("/api/v1/tracks")
                            .with(httpBasic(TEST_USER, TEST_PASSWORD))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"isrCode\":\"\"}"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/tracks/{isrCode}/cover")
    class GetCover {

        @Test
        @DisplayName("returns 200 and image when found")
        void returns200WhenFound() throws Exception {
            byte[] imageBytes = new byte[]{1, 2, 3};
            when(getCoverImageUseCase.getCoverImage(ISRC)).thenReturn(imageBytes);

            mockMvc.perform(get("/api/v1/tracks/{isrCode}/cover", ISRC)
                            .with(httpBasic(TEST_USER, TEST_PASSWORD))
                            .accept(MediaType.IMAGE_JPEG_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_JPEG));
        }

        @Test
        @DisplayName("returns 404 when track not found")
        void returns404WhenNotFound() throws Exception {
            when(getCoverImageUseCase.getCoverImage(ISRC))
                    .thenThrow(new TrackNotFoundException("Track not found: " + ISRC));

            mockMvc.perform(get("/api/v1/tracks/{isrCode}/cover", ISRC)
                            .with(httpBasic(TEST_USER, TEST_PASSWORD))
                            .accept(MediaType.IMAGE_JPEG_VALUE))
                    .andExpect(status().isNotFound());
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
                .coverPath("/path/cover.jpg")
                .build();
    }

    private static TrackDto toDto(Track track) {
        return new TrackDto(
                track.getIsrCode(),
                track.getName(),
                track.getArtistName(),
                track.getAlbumName(),
                track.getAlbumId(),
                track.isExplicit(),
                track.getPlaybackSeconds(),
                track.getCoverPath()
        );
    }
}
