package com.fbaron.tracker.data.jpa.mapper;

import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.data.jpa.entity.TrackJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrackJpaMapper {

    @Mapping(target = "isExplicit", source = "explicit")
    TrackJpaEntity toEntity(Track track);

    @Mapping(target = "isExplicit", source = "explicit")
    Track toModel(TrackJpaEntity source);

}
