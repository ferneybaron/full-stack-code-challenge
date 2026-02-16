"use client";

import { useState, useEffect } from "react";
import { Clock, Disc3, AlertTriangle, ChevronRight } from "lucide-react";
import type { Track } from "../../../app/tracks/model/Track";
import { useGetTrackCoverQuery } from "../../../app/tracks/service/trackService";
import { Link } from "react-router";
import { Card } from "../cards/card";
import { Badge } from "../badges/badge";

interface TrackListProps {
  tracks: Track[];
}

export function TrackList({ tracks }: TrackListProps) {
  if (tracks.length === 0) return null;

  return (
    <div className="flex flex-col gap-3">
      <h3 className="text-sm font-medium text-muted-foreground">
        {tracks.length} {tracks.length === 1 ? "result" : "results"} found
      </h3>
      <div className="flex flex-col gap-2">
        {tracks.map((track) => (
          <TrackListItem key={track.isrCode} track={track} />
        ))}
      </div>
    </div>
  );
}

function TrackListItem({ track }: { track: Track }) {
  const [imgError, setImgError] = useState(false);
  const { data: coverBlob } = useGetTrackCoverQuery(track.isrCode, {
    skip: !track.isrCode,
  });
  const [coverUrl, setCoverUrl] = useState<string>("");

  useEffect(() => {
    if (!coverBlob) {
      queueMicrotask(() => setCoverUrl(""));
      return () => {};
    }
    const url = URL.createObjectURL(coverBlob);
    let cancelled = false;
    queueMicrotask(() => {
      if (!cancelled) setCoverUrl(url);
    });
    return () => {
      cancelled = true;
      URL.revokeObjectURL(url);
    };
  }, [coverBlob]);

  function formatDuration(seconds: number): string {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, "0")}`;
  }

  return (
    <Link to={`/tracks/${encodeURIComponent(track.isrCode)}`}>
      <Card className="border-border/50 bg-card/80 hover:bg-secondary/50 transition-colors cursor-pointer group">
        <div className="flex items-center gap-4 p-3 sm:p-4">
          {/* Cover thumbnail */}
          <div className="relative w-14 h-14 sm:w-16 sm:h-16 rounded-md overflow-hidden flex-shrink-0 bg-secondary">
            {!imgError && coverUrl ? (
              <img
                src={coverUrl}
                alt={`Album cover for ${track.albumName}`}
                className="w-full h-full object-cover"
                onError={() => setImgError(true)}
              />
            ) : (
              <div className="w-full h-full flex items-center justify-center text-muted-foreground">
                <Disc3 className="w-6 h-6" />
              </div>
            )}
          </div>

          {/* Track info */}
          <div className="flex-1 min-w-0 flex flex-col gap-1">
            <div className="flex items-center gap-2">
              <h4 className="text-sm sm:text-base font-semibold text-foreground truncate">
                {track.name}
              </h4>
              {track.isExplicit && (
                <Badge
                  variant="destructive"
                  className="text-[10px] px-1.5 py-0 h-4 gap-0.5 flex-shrink-0"
                >
                  <AlertTriangle className="w-2.5 h-2.5" />E
                </Badge>
              )}
            </div>
            <p className="text-xs sm:text-sm text-muted-foreground truncate">
              {track.artistName} &middot; {track.albumName}
            </p>
            <div className="flex items-center gap-3 mt-0.5">
              <span className="font-mono text-[10px] sm:text-xs text-primary/80 bg-primary/5 px-1.5 py-0.5 rounded">
                {track.isrCode}
              </span>
              <span className="flex items-center gap-1 text-[10px] sm:text-xs text-muted-foreground">
                <Clock className="w-3 h-3" />
                {formatDuration(track.playbackSeconds)}
              </span>
            </div>
          </div>

          {/* Arrow */}
          <ChevronRight className="w-5 h-5 text-muted-foreground group-hover:text-primary transition-colors flex-shrink-0" />
        </div>
      </Card>
    </Link>
  );
}
