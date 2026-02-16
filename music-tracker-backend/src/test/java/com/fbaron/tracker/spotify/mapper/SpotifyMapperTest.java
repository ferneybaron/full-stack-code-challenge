package com.fbaron.tracker.spotify.mapper;

import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.data.spotify.entity.SpotifyAlbum;
import com.fbaron.tracker.data.spotify.entity.SpotifyArtist;
import com.fbaron.tracker.data.spotify.entity.SpotifyTrackItem;
import com.fbaron.tracker.data.spotify.mapper.SpotifyMapper;
import com.fbaron.tracker.data.spotify.mapper.SpotifyMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SpotifyMapperTest {

    private SpotifyMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SpotifyMapperImpl();
    }

    @Test
    @DisplayName("toModel maps SpotifyTrackItem and isrCode to Track with correct fields")
    void toModel_mapsAllFields() {
        SpotifyTrackItem item = new SpotifyTrackItem(
                "Track Name",
                true,
                185000L,
                new SpotifyAlbum("album-123", "Album Name", List.of()),
                List.of(new SpotifyArtist("Artist Name"))
        );
        String isrCode = "USMC18620549";

        Track track = mapper.toModel(item, isrCode);

        assertThat(track).isNotNull();
        assertThat(track.getIsrCode()).isEqualTo(isrCode);
        assertThat(track.getName()).isEqualTo("Track Name");
        assertThat(track.getArtistName()).isEqualTo("Artist Name");
        assertThat(track.getAlbumName()).isEqualTo("Album Name");
        assertThat(track.getAlbumId()).isEqualTo("album-123");
        assertThat(track.isExplicit()).isTrue();
        assertThat(track.getPlaybackSeconds()).isEqualTo(185L); // 185000 ms -> 185 s
        assertThat(track.getCoverPath()).isNull();
    }

    @Test
    @DisplayName("toModel uses msToSeconds for duration (rounds down)")
    void toModel_convertsDurationCorrectly() {
        SpotifyTrackItem item = new SpotifyTrackItem(
                "Track",
                false,
                1999L,
                new SpotifyAlbum("id", "Album", List.of()),
                List.of(new SpotifyArtist("Artist"))
        );

        Track track = mapper.toModel(item, "ISRC123");

        assertThat(track.getPlaybackSeconds()).isEqualTo(1L);
    }
}
