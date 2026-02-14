package com.fbaron.tracker.web.rest;

import com.fbaron.tracker.core.model.RegistrationResult;
import com.fbaron.tracker.core.usecase.RegisterTrackUseCase;
import com.fbaron.tracker.web.dto.RegisterTrackDto;
import com.fbaron.tracker.web.dto.TrackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tracks")
public class TrackRestAdapter implements TrackRestApi {

    private final RegisterTrackUseCase registerTrackUseCase;

    @Override
    @PostMapping
    public ResponseEntity<TrackDto> registerTrack(@RequestBody RegisterTrackDto dto) {
        RegistrationResult result  =  registerTrackUseCase.register(dto.isrCode());
        return null;
    }

}
