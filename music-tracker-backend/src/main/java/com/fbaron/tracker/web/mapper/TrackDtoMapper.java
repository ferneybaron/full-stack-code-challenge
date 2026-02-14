package com.fbaron.tracker.web.mapper;

import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.web.dto.TrackDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrackDtoMapper {

    @Mapping(target = "isExplicit", source = "explicit")
    TrackDto toDto(Track track);

}
