package com.fbaron.tracker.web.rest;

import com.fbaron.tracker.core.model.RegistrationResult;
import com.fbaron.tracker.core.usecase.RegisterTrackUseCase;
import com.fbaron.tracker.web.dto.RegisterTrackDto;
import com.fbaron.tracker.web.dto.TrackDto;
import com.fbaron.tracker.web.mapper.TrackDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tracks")
public class TrackRestAdapter implements TrackRestApi {

    private final RegisterTrackUseCase registerTrackUseCase;
    private final TrackDtoMapper trackDtoMapper;

    @Override
    @PostMapping
    public ResponseEntity<TrackDto> registerTrack(@RequestBody RegisterTrackDto dto) {
        RegistrationResult result  =  registerTrackUseCase.register(dto.isrCode());
        TrackDto trackDto = trackDtoMapper.toDto(result.track());
        if (result.saved()) {
            log.info("Track created: isCode={}", trackDto.isrCode());
            return ResponseEntity.status(HttpStatus.CREATED).body(trackDto);
        }
        log.info("Track already existed, returning: isCode={}", trackDto.isrCode());
        return ResponseEntity.ok(trackDto);
    }

}
