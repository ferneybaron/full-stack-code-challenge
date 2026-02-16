package com.fbaron.tracker;

import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.core.repository.MusicProviderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Integration test: full Spring context, H2 DB, real REST/JPA/storage.
 * Spotify is mocked via @MockBean MusicProviderRepository so no real API calls.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TrackApiIntegrationTest {

    private static final String ISRC = "USMC18620549";
    private static final String BASE = "/api/v1/tracks";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private MusicProviderRepository musicProviderRepository;

    @Test
    @DisplayName("register then get metadata and cover – Spotify mocked, H2 + storage real")
    void registerThenGetMetadataAndCover() {
        Track fromProvider = Track.builder()
                .isrCode(ISRC)
                .name("Integration Test Track")
                .artistName("Test Artist")
                .albumName("Test Album")
                .albumId("album-123")
                .isExplicit(false)
                .playbackSeconds(180L)
                .coverPath(null)
                .build();
        byte[] coverBytes = new byte[]{0x00, (byte) 0xFF, 0x0A};

        when(musicProviderRepository.fetchMetadata(ISRC)).thenReturn(fromProvider);
        when(musicProviderRepository.fetchCoverImage(eq("album-123"))).thenReturn(coverBytes);

        TestRestTemplate authenticated = restTemplate.withBasicAuth("testuser", "testpass");

        // Register
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> registerRequest = new HttpEntity<>("{\"isrCode\":\"" + ISRC + "\"}", headers);
        ResponseEntity<String> registerResponse = authenticated.postForEntity(BASE, registerRequest, String.class);

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(registerResponse.getBody()).contains(ISRC).contains("Integration Test Track");

        // Get metadata
        ResponseEntity<String> getResponse = authenticated.getForEntity(BASE + "/" + ISRC, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).contains(ISRC).contains("Integration Test Track");

        // Get cover
        ResponseEntity<byte[]> coverResponse = authenticated.getForEntity(BASE + "/" + ISRC + "/cover", byte[].class);
        assertThat(coverResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(coverResponse.getBody()).isEqualTo(coverBytes);
    }

    @Test
    @DisplayName("get track without auth returns 401")
    void getTrackWithoutAuthReturns401() {
        ResponseEntity<String> response = restTemplate.getForEntity(BASE + "/" + ISRC, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
