package com.fbaron.tracker.data.spotify;

import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.core.repository.MusicProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpotifyAdapter implements MusicProviderRepository {

    private final RestClient spotifyRestClient;

    @Value("${spotify.auth-url}")
    private String authUrl;

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    private synchronized String getAccessToken() {
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
            throw new RuntimeException("Failed to obtain Spotify access token");
        }

        log.info("Spotify Access Token Fetched");
        return (String) response.get("access_token");
    }

    @Override
    public Track fetchMetadata(String isrCode) {
        String token = getAccessToken();
        return null;
    }

    @Override
    public byte[] fetchCoverImage(String albumId) {
        return new byte[0];
    }

}
