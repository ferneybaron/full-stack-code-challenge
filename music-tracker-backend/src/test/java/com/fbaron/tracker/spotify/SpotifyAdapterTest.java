package com.fbaron.tracker.spotify;

import com.fbaron.tracker.core.exception.ImageNotFoundException;
import com.fbaron.tracker.core.exception.ProviderAuthenticationException;
import com.fbaron.tracker.core.exception.TrackNotFoundException;
import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.data.spotify.SpotifyAdapter;
import com.fbaron.tracker.data.spotify.entity.SpotifyAlbum;
import com.fbaron.tracker.data.spotify.entity.SpotifyAlbumResponse;
import com.fbaron.tracker.data.spotify.entity.SpotifyArtist;
import com.fbaron.tracker.data.spotify.entity.SpotifyImage;
import com.fbaron.tracker.data.spotify.entity.SpotifySearchResponse;
import com.fbaron.tracker.data.spotify.entity.SpotifyTrackItem;
import com.fbaron.tracker.data.spotify.entity.SpotifyTrackList;
import com.fbaron.tracker.data.spotify.mapper.SpotifyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/** Unit tests for {@link SpotifyAdapter}: fetchMetadata and fetchCoverImage with mocked RestClient. */
@ExtendWith(MockitoExtension.class)
class SpotifyAdapterTest {

    private static final String ISRC = "USMC18620549";

    @Mock
    private RestClient spotifyRestClient;
    @Mock
    @SuppressWarnings({"rawtypes", "unchecked"})
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    @SuppressWarnings({"rawtypes", "unchecked"})
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RestClient.RequestBodySpec requestBodySpec;
    @Mock
    @SuppressWarnings({"rawtypes", "unchecked"})
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private SpotifyMapper spotifyTrackMapper;

    private SpotifyAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        adapter = new SpotifyAdapter(spotifyRestClient, spotifyTrackMapper);
        setField(adapter, "authUrl", "https://accounts.spotify.com/api/token");
        setField(adapter, "clientId", "test-client-id");
        setField(adapter, "clientSecret", "test-client-secret");
    }

    private static Map<String, Object> tokenResponse() {
        return Map.of("access_token", "test-token", "expires_in", 3600);
    }

    private void stubToken() {
        when(spotifyRestClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(eq(Map.class))).thenReturn(tokenResponse());
    }

    @Nested
    @DisplayName("fetchMetadata")
    class FetchMetadata {

        @Test
        @DisplayName("returns Track when search finds items")
        void returnsTrackWhenFound() {
            stubToken();

            SpotifyTrackItem spotifyItem = new SpotifyTrackItem(
                    "Test Track",
                    false,
                    180000L,
                    new SpotifyAlbum("album-123", "Test Album", List.of()),
                    List.of(new SpotifyArtist("Test Artist"))
            );
            SpotifySearchResponse searchResponse = new SpotifySearchResponse(
                    new SpotifyTrackList(List.of(spotifyItem))
            );
            Track expectedTrack = Track.builder()
                    .isrCode(ISRC)
                    .name("Test Track")
                    .artistName("Test Artist")
                    .albumName("Test Album")
                    .albumId("album-123")
                    .playbackSeconds(180L)
                    .build();

            when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifySearchResponse.class)).thenReturn(searchResponse);
            when(spotifyTrackMapper.toModel(eq(spotifyItem), eq(ISRC))).thenReturn(expectedTrack);

            Track result = adapter.fetchMetadata(ISRC);

            assertThat(result).isNotNull();
            assertThat(result.getIsrCode()).isEqualTo(ISRC);
            assertThat(result.getName()).isEqualTo("Test Track");
            assertThat(result.getArtistName()).isEqualTo("Test Artist");
            assertThat(result.getAlbumName()).isEqualTo("Test Album");
            assertThat(result.getAlbumId()).isEqualTo("album-123");
            assertThat(result.getPlaybackSeconds()).isEqualTo(180L);
        }

        @Test
        @DisplayName("throws TrackNotFoundException when search returns no items")
        void throwsWhenNoTracks() {
            stubToken();

            SpotifySearchResponse emptyResponse = new SpotifySearchResponse(new SpotifyTrackList(List.of()));

            when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifySearchResponse.class)).thenReturn(emptyResponse);

            assertThatThrownBy(() -> adapter.fetchMetadata("UNKNOWN"))
                    .isInstanceOf(TrackNotFoundException.class)
                    .hasMessageContaining("UNKNOWN");
        }

        @Test
        @DisplayName("throws TrackNotFoundException when search response is null")
        void throwsWhenResponseNull() {
            stubToken();

            when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifySearchResponse.class)).thenReturn(null);

            assertThatThrownBy(() -> adapter.fetchMetadata(ISRC))
                    .isInstanceOf(TrackNotFoundException.class)
                    .hasMessageContaining(ISRC);
        }

        @Test
        @DisplayName("throws ProviderAuthenticationException when token response has no access_token")
        void throwsWhenTokenFails() throws Exception {
            when(spotifyRestClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(eq(Map.class))).thenReturn(Map.of("error", "invalid_client"));

            setField(adapter, "cachedToken", null);
            setField(adapter, "expiryTime", null);

            assertThatThrownBy(() -> adapter.fetchMetadata(ISRC))
                    .isInstanceOf(ProviderAuthenticationException.class)
                    .hasMessageContaining("Failed to obtain Spotify access token");
        }
    }

    @Nested
    @DisplayName("fetchCoverImage")
    class FetchCoverImage {

        private static final String ALBUM_ID = "album-123";
        private static final String IMAGE_URL = "https://i.scdn.co/image/cover";

        @Test
        @DisplayName("returns cover image bytes when album has images")
        void returnsCoverImageWhenAlbumHasImages() {
            stubToken();

            SpotifyAlbumResponse albumResponse = new SpotifyAlbumResponse(
                    List.of(new SpotifyImage(IMAGE_URL))
            );
            byte[] coverBytes = new byte[]{1, 2, 3};

            when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(eq("/v1/albums/{albumId}"), eq(ALBUM_ID))).thenReturn(requestHeadersSpec);
            when(requestHeadersUriSpec.uri(eq(IMAGE_URL))).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifyAlbumResponse.class)).thenReturn(albumResponse);
            when(responseSpec.body(byte[].class)).thenReturn(coverBytes);

            byte[] result = adapter.fetchCoverImage(ALBUM_ID);

            assertThat(result).isEqualTo(coverBytes);
        }

        @Test
        @DisplayName("throws ImageNotFoundException when album response is null")
        void throwsWhenAlbumResponseNull() {
            stubToken();

            when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(eq("/v1/albums/{albumId}"), eq(ALBUM_ID))).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifyAlbumResponse.class)).thenReturn(null);

            assertThatThrownBy(() -> adapter.fetchCoverImage(ALBUM_ID))
                    .isInstanceOf(ImageNotFoundException.class)
                    .hasMessageContaining(ALBUM_ID);
        }

        @Test
        @DisplayName("throws ImageNotFoundException when album has no images")
        void throwsWhenAlbumHasNoImages() {
            stubToken();

            SpotifyAlbumResponse albumResponse = new SpotifyAlbumResponse(List.of());

            when(spotifyRestClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(eq("/v1/albums/{albumId}"), eq(ALBUM_ID))).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifyAlbumResponse.class)).thenReturn(albumResponse);

            assertThatThrownBy(() -> adapter.fetchCoverImage(ALBUM_ID))
                    .isInstanceOf(ImageNotFoundException.class)
                    .hasMessageContaining(ALBUM_ID);
        }

        @Test
        @DisplayName("throws ProviderAuthenticationException when token fails")
        void throwsWhenTokenFails() throws Exception {
            when(spotifyRestClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(eq(Map.class))).thenReturn(Map.of("error", "invalid_client"));

            setField(adapter, "cachedToken", null);
            setField(adapter, "expiryTime", null);

            assertThatThrownBy(() -> adapter.fetchCoverImage(ALBUM_ID))
                    .isInstanceOf(ProviderAuthenticationException.class)
                    .hasMessageContaining("Failed to obtain Spotify access token");
        }
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = SpotifyAdapter.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
