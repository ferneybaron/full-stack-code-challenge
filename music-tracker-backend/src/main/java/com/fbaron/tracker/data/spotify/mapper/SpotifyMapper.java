package com.fbaron.tracker.data.spotify.mapper;

import com.fbaron.tracker.core.model.Track;
import com.fbaron.tracker.data.spotify.entity.SpotifyTrackItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SpotifyMapper {

    @Mapping(target = "isrCode", source = "isrCode")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "artistName", expression = "java(item.artists().getFirst().name())")
    @Mapping(target = "albumName", source = "item.album.name")
    @Mapping(target = "albumId", source = "item.album.id")
    @Mapping(target = "isExplicit", source = "item.explicit")
    @Mapping(target = "playbackSeconds", source = "item.duration_ms", qualifiedByName = "msToSeconds")
    @Mapping(target = "coverPath", ignore = true)
    Track toModel(SpotifyTrackItem item, String isrCode);


    @Named("msToSeconds")
    default long msToSeconds(long ms) {
        return ms / 1000;
    }

}
