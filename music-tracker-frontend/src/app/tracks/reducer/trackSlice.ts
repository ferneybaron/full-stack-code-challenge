import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "../../config/store";
import type { Track } from "../model/Track";

export interface TrackState {
  track: Track | null;
  tracks: Track[] | undefined;
  status: "idle" | "loading" | "failed";
}

const initialState: TrackState = {
  track: null,
  tracks: undefined,
  status: "idle",
};

export const trackSlice = createSlice({
  name: "trackState",
  initialState,
  reducers: {
    initTrack(state) {
      state.track = initialState.track;
    },
    setTrack(state, action: PayloadAction<Track | null>) {
      state.track = action.payload;
    },
    setTrackList(state, action: PayloadAction<Track[] | undefined>) {
      state.tracks = action.payload;
    },
  },
});

export const { initTrack, setTrack, setTrackList } = trackSlice.actions;
export const selectTrack = (state: RootState) => state.trackState.track;
export const selectTracks = (state: RootState) => state.trackState.tracks;

export default trackSlice.reducer;
