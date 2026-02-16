package com.fbaron.tracker.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrackTest {

    private static final String ISRC = "USMC18620549";
    private static final String COVER_PATH = "/storage/covers/USMC18620549.jpg";

    @Nested
    @DisplayName("builder")
    class Builder {

        @Test
        @DisplayName("builds Track with all fields")
        void buildsWithAllFields() {
            Track track = Track.builder()
                    .isrCode(ISRC)
                    .name("Track Name")
                    .artistName("Artist")
                    .albumName("Album")
                    .albumId("album-123")
                    .isExplicit(true)
                    .playbackSeconds(180L)
                    .coverPath(COVER_PATH)
                    .build();

            assertThat(track.getIsrCode()).isEqualTo(ISRC);
            assertThat(track.getName()).isEqualTo("Track Name");
            assertThat(track.getArtistName()).isEqualTo("Artist");
            assertThat(track.getAlbumName()).isEqualTo("Album");
            assertThat(track.getAlbumId()).isEqualTo("album-123");
            assertThat(track.isExplicit()).isTrue();
            assertThat(track.getPlaybackSeconds()).isEqualTo(180L);
            assertThat(track.getCoverPath()).isEqualTo(COVER_PATH);
        }

        @Test
        @DisplayName("builds Track with partial fields")
        void buildsWithPartialFields() {
            Track track = Track.builder()
                    .isrCode(ISRC)
                    .name("Track Name")
                    .build();

            assertThat(track.getIsrCode()).isEqualTo(ISRC);
            assertThat(track.getName()).isEqualTo("Track Name");
            assertThat(track.getArtistName()).isNull();
            assertThat(track.getCoverPath()).isNull();
        }
    }

    @Nested
    @DisplayName("equals and hashCode")
    class EqualsAndHashCode {

        @Test
        @DisplayName("tracks with same field values are equal")
        void equalWhenSameValues() {
            Track a = Track.builder().isrCode(ISRC).name("Track").build();
            Track b = Track.builder().isrCode(ISRC).name("Track").build();

            assertThat(a).isEqualTo(b);
            assertThat(a.hashCode()).isEqualTo(b.hashCode());
        }

        @Test
        @DisplayName("tracks with different isrCode are not equal")
        void notEqualWhenDifferentIsrCode() {
            Track a = Track.builder().isrCode(ISRC).name("Track").build();
            Track b = Track.builder().isrCode("OTHER").name("Track").build();

            assertThat(a).isNotEqualTo(b);
        }
    }
}
