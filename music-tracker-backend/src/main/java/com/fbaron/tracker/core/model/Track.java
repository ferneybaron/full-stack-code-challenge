package com.fbaron.tracker.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Track {

    private String isrCode;
    private String name;
    private String artistName;
    private String albumName;
    private String albumId;
    private boolean isExplicit;
    private Long playbackSeconds;
    private String coverPath;

}
