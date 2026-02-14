package com.fbaron.tracker.data.jpa;

import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.core.repository.TrackCommandRepository;
import com.fbaron.tracker.core.repository.TrackQueryRepository;
import com.fbaron.tracker.data.jpa.entity.TrackJpaEntity;
import com.fbaron.tracker.data.jpa.mapper.TrackJpaMapper;
import com.fbaron.tracker.data.jpa.repository.TrackJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackJpaAdapter implements TrackQueryRepository, TrackCommandRepository {

    private final TrackJpaRepository trackJpaRepository;
    private final TrackJpaMapper trackJpaMapper;


    @Override
    public Optional<Track> findByIsrCode(String isrCode) {
        log.info("findByIsrCode isrCode={}", isrCode);
        return trackJpaRepository.findById(isrCode)
                .map(trackJpaMapper::toModel);
    }

    @Override
    public Track save(Track track) {
        log.info("Saving Tack isrCode={}", track.getIsrCode());
        TrackJpaEntity trackJpaEntity = trackJpaMapper.toEntity(track);
        trackJpaEntity = trackJpaRepository.save(trackJpaEntity);
        log.info("Saved Track isrCode={}", trackJpaEntity.getIsrCode());
        return trackJpaMapper.toModel(trackJpaEntity);
    }

}
