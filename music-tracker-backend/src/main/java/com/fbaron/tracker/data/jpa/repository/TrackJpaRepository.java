package com.fbaron.tracker.data.jpa.repository;

import com.fbaron.tracker.data.jpa.entity.TrackJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackJpaRepository extends JpaRepository<TrackJpaEntity, String> {
}
