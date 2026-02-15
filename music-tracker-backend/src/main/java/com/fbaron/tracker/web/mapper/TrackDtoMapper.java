package com.fbaron.tracker.web.mapper;

import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.web.dto.TrackDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps domain {@link Track} to {@link com.fbaron.tracker.web.dto.TrackDto} (isExplicit field name).
 */
@Mapper(componentModel = "spring")
public interface TrackDtoMapper {

    @Mapping(target = "isExplicit", source = "explicit")
    TrackDto toDto(Track track);

}
