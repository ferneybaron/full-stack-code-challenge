package com.fbaron.tracker.data.spotify;

import com.fbaron.tracker.core.exception.ImageNotFoundException;
import com.fbaron.tracker.core.exception.ProviderAuthenticationException;
import com.fbaron.tracker.core.exception.TrackNotFoundException;
import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.core.repository.MusicProviderRepository;
import com.fbaron.tracker.data.spotify.entity.SpotifyAlbumResponse;
import com.fbaron.tracker.data.spotify.entity.SpotifySearchResponse;
import com.fbaron.tracker.data.spotify.mapper.SpotifyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpotifyAdapter implements MusicProviderRepository {

    private final RestClient spotifyRestClient;
    private final SpotifyMapper spotifyMapper;

    @Value("${spotify.auth-url}")
    private String authUrl;

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    private String cachedToken;
    private Instant expiryTime;

    private synchronized String getAccessToken() {
        if (cachedToken != null && Instant.now().isBefore(expiryTime)) return cachedToken;

        log.info("Fetching Spotify Access Token...");
        MultiValueMap<String, String> formData =  new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");

        var response = spotifyRestClient.post()
                .uri(authUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(clientId, clientSecret))
                .body(formData)
                .retrieve()
                .body(Map.class);

        if (response == null || !response.containsKey("access_token")) {
            log.error("Failed to obtain Spotify access token: response missing access_token");
            throw new ProviderAuthenticationException("Failed to obtain Spotify access token");
        }

        log.info("Spotify Access Token Fetched");
        this.cachedToken = (String) response.get("access_token");
        Object expiresInObj = response.get("expires_in");
        int expiresInSeconds = expiresInObj instanceof Number n ? n.intValue() : 3600;
        this.expiryTime = Instant.now().plusSeconds(Math.max(0, expiresInSeconds - 60));
        return cachedToken;
    }

    @Override
    public Track fetchMetadata(String isrCode) {
        String token = getAccessToken();

        if (token == null || token.isBlank()) {
            throw new ProviderAuthenticationException("Spotify Access Token is missing");
        }

        SpotifySearchResponse response = spotifyRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search")
                        .queryParam("q", "isrc:" + isrCode)
                        .queryParam("type", "track")
                        .build())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .body((SpotifySearchResponse.class));

        if (response == null || response.tracks().items().isEmpty()) {
            log.warn("Track not found in Spotify: isrCode = {}", isrCode);
            throw new TrackNotFoundException("Track not found with IRSC: " + isrCode);
        }

        var trackItem  = response.tracks().items().getFirst();
        Track track = spotifyMapper.toModel(trackItem, isrCode);
        log.debug("Fetched metadata form Spotify: isrCode={}, name={}", isrCode, track.getName());
        return track;
    }

    @Override
    public byte[] fetchCoverImage(String albumId) {
        String token = getAccessToken();

        SpotifyAlbumResponse albumResponse = spotifyRestClient.get()
                .uri("/v1/albums/{albumId}", albumId)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                .retrieve()
                .body(SpotifyAlbumResponse.class);

        if (albumResponse == null || albumResponse.images().isEmpty()) {
            log.warn("Album not found in Spotify: albumId = {}", albumId);
            throw new ImageNotFoundException("No images found for album: " + albumId);
        }

        String imageUrl = albumResponse.images().getFirst().url();
        log.info("Fetching cover image for album={}", albumId);

        return spotifyRestClient.get()
                .uri(imageUrl)
                .retrieve()
                .body(byte[].class);
    }

}
