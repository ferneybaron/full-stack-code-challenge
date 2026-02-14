package com.fbaron.tracker.data.spotify.entity;

import java.util.List;

public record SpotifyAlbum(String id, String name, List<SpotifyImage> images) {
}
