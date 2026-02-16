import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import type { RootState } from "../../config/store";
import type { Track } from "../model/Track";

const BASE_URL =
  (import.meta.env.VITE_BACKEND_API ?? "http://localhost:8080/tracker") +
  "/api/v1";

export const trackService = createApi({
  reducerPath: "trackService",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
    prepareHeaders: (headers, { getState }) => {
      const auth = (getState() as RootState).auth;
      const username = auth.usermane || "";
      const password = auth.password || "";
      if (username || password) {
        headers.set(
          "Authorization",
          `Basic ${btoa(`${username}:${password}`)}`,
        );
      }
      return headers;
    },
  }),
  tagTypes: ["Tracks"],
  endpoints: (builder) => ({
    /** GET /api/v1/tracks/{isrCode} - stored track metadata */
    getTrackByIsrCode: builder.query<Track, string>({
      query: (isrCode) => `/tracks/${encodeURIComponent(isrCode)}`,
      providesTags: (_result, _error, isrCode) => [
        { type: "Tracks", id: isrCode },
      ],
    }),
    /** POST /api/v1/tracks - body: { isrCode }. Fetches from provider and stores. 201 created, 200 already exists. */
    registerTrack: builder.mutation<Track, { isrCode: string }>({
      query: (body) => ({
        url: "/tracks",
        method: "POST",
        body: { isrCode: body.isrCode },
      }),
      invalidatesTags: (_result, _error, { isrCode }) => [
        { type: "Tracks", id: isrCode },
        { type: "Tracks", id: "LIST" },
      ],
    }),
    /** GET /api/v1/tracks/{isrCode}/cover - cover image (JPEG blob) for use in <img> via object URL */
    getTrackCover: builder.query<Blob, string>({
      query: (isrCode) => ({
        url: `/tracks/${encodeURIComponent(isrCode)}/cover`,
        responseHandler: (response) => response.blob(),
      }),
      providesTags: (_result, _error, isrCode) => [
        { type: "Tracks", id: `${isrCode}-cover` },
      ],
    }),
  }),
});

export const {
  useGetTrackByIsrCodeQuery,
  useRegisterTrackMutation,
  useGetTrackCoverQuery,
  useLazyGetTrackByIsrCodeQuery,
  useLazyGetTrackCoverQuery,
} = trackService;
