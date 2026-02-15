package com.fbaron.tracker.data.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity for the tracks table (ISRC as primary key, metadata and cover path).
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tracks")
public class TrackJpaEntity {

    @Id
    private String isrCode;
    private String name;
    private String artistName;
    private String albumName;
    private String albumId;
    private boolean isExplicit;
    private Long playbackSeconds;
    private String coverPath;

}
