package com.fbaron.tracker.web.rest;

import com.fbaron.tracker.web.dto.RegisterTrackDto;
import com.fbaron.tracker.web.dto.TrackDto;
import org.springframework.http.ResponseEntity;

// Document later
public interface TrackRestApi {

    ResponseEntity<TrackDto> registerTrack(RegisterTrackDto dto);

    ResponseEntity<TrackDto> getTrackMetadata(String isrCode);

    ResponseEntity<byte[]> getCover(String isrCode);

}
