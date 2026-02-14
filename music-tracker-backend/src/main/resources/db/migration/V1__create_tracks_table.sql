CREATE TABLE tracks (
    isr_code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    artist_name VARCHAR(50) NOT NULL,
    album_name VARCHAR(50) NOT NULL,
    album_id VARCHAR(100) NOT NULL,
    is_explicit BOOLEAN DEFAULT FALSE,
    playback_seconds BIGINT NOT NULL,
    cover_path VARCHAR(512)
);

CREATE INDEX idx_tracks_album_id ON tracks(album_id);