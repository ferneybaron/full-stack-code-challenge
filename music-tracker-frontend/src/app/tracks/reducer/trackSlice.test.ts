import { describe, it, expect } from "vitest";
import trackReducer, { initTrack, setTrack, setTrackList } from "./trackSlice";
import type { Track } from "../model/Track";

const mockTrack: Track = {
  isrCode: "USMC18620549",
  name: "Test Track",
  artistName: "Artist",
  albumName: "Album",
  albumId: "album-1",
  isExplicit: false,
  playbackSeconds: 180,
  coverPath: null,
};

describe("trackSlice", () => {
  it("returns initial state", () => {
    expect(trackReducer(undefined, { type: "unknown" })).toEqual({
      track: null,
      tracks: undefined,
      status: "idle",
    });
  });

  it("setTrack sets the current track", () => {
    const state = trackReducer(undefined, setTrack(mockTrack));
    expect(state.track).toEqual(mockTrack);
  });

  it("setTrack with null clears the track", () => {
    const withTrack = trackReducer(undefined, setTrack(mockTrack));
    const state = trackReducer(withTrack, setTrack(null));
    expect(state.track).toBeNull();
  });

  it("setTrackList sets the tracks array", () => {
    const tracks = [mockTrack];
    const state = trackReducer(undefined, setTrackList(tracks));
    expect(state.tracks).toEqual(tracks);
  });

  it("setTrackList with undefined clears the list", () => {
    const withTracks = trackReducer(undefined, setTrackList([mockTrack]));
    const state = trackReducer(withTracks, setTrackList(undefined));
    expect(state.tracks).toBeUndefined();
  });

  it("initTrack resets track to initial", () => {
    const withTrack = trackReducer(undefined, setTrack(mockTrack));
    const state = trackReducer(withTrack, initTrack());
    expect(state.track).toBeNull();
  });
});
