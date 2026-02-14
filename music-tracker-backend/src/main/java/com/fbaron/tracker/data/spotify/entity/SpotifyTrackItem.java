package com.fbaron.tracker.data.spotify.entity;

import java.util.List;

public record SpotifyTrackItem(String name,
                               boolean explicit,
                               long duration_ms,
                               SpotifyAlbum album,
                               List<SpotifyArtist> artists) {
}
