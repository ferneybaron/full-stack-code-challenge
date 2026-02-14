package com.fbaron.tracker.data.jpa;

import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.core.repository.TrackQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackJpaAdapter implements TrackQueryRepository {


    @Override
    public Optional<Track> findByIsrCode(String isrCode) {
        log.info("findByIsrCode not implemented yet");
        return Optional.empty();
    }

}
