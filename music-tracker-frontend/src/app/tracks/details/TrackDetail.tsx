import { useState, useEffect } from "react";
import { useParams, Link } from "react-router";
import {
  Clock,
  Disc3,
  Mic2,
  Album,
  AlertTriangle,
  ArrowLeft,
} from "lucide-react";
import type { Track } from "../model/Track";
import {
  useGetTrackByIsrCodeQuery,
  useGetTrackCoverQuery,
} from "../service/trackService";
import { useAppSelector } from "../../config/hooks";
import { selectTrack, selectTracks } from "../reducer/trackSlice";
import { Card, CardContent } from "../../../shared/components/cards/card";
import { Badge } from "../../../shared/components/badges/badge";
import { TrackSkeleton } from "../../../shared/components/skeletons/track-skeleton";
import { Button } from "../../../shared/components/buttons/button";

export function TrackDetail() {
  const { id: isrCode } = useParams<{ id: string }>();
  const trackFromStore = useAppSelector(selectTrack);
  const tracksFromStore = useAppSelector(selectTracks);

  const trackInStore =
    trackFromStore?.isrCode === isrCode
      ? trackFromStore
      : (tracksFromStore?.find((t) => t.isrCode === isrCode) ?? null);

  const {
    data: trackFromApi,
    isLoading,
    isError,
  } = useGetTrackByIsrCodeQuery(isrCode ?? "", {
    skip: !isrCode || !!trackInStore,
  });

  const track = trackInStore ?? trackFromApi ?? null;

  if (!isrCode) {
    return (
      <div className="min-h-screen flex flex-col">
        <main className="flex-1 mx-auto max-w-5xl w-full px-4 sm:px-6 py-8">
          <p className="text-muted-foreground">No track specified.</p>
          <Button asChild variant="link" className="mt-4 p-0">
            <Link to="/tracks">
              <ArrowLeft className="w-4 h-4 mr-2 inline" />
              Back to Tracks
            </Link>
          </Button>
        </main>
      </div>
    );
  }

  if (isLoading && !track) {
    return (
      <div className="min-h-screen flex flex-col">
        <main className="flex-1 mx-auto max-w-5xl w-full px-4 sm:px-6 py-8 flex flex-col gap-6">
          <Button asChild variant="ghost" size="sm" className="w-fit">
            <Link to="/tracks">
              <ArrowLeft className="w-4 h-4 mr-2" />
              Back to Tracks
            </Link>
          </Button>
          <TrackSkeleton />
        </main>
      </div>
    );
  }

  if (isError || !track) {
    return (
      <div className="min-h-screen flex flex-col">
        <main className="flex-1 mx-auto max-w-5xl w-full px-4 sm:px-6 py-8 flex flex-col gap-4">
          <Button asChild variant="ghost" size="sm" className="w-fit">
            <Link to="/tracks">
              <ArrowLeft className="w-4 h-4 mr-2" />
              Back to Tracks
            </Link>
          </Button>
          <p className="text-muted-foreground">Track not found.</p>
        </main>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex flex-col">
      <main className="flex-1 mx-auto max-w-5xl w-full px-4 sm:px-6 py-8 flex flex-col gap-6">
        <Button asChild variant="ghost" size="sm" className="w-fit">
          <Link to="/tracks">
            <ArrowLeft className="w-4 h-4 mr-2" />
            Back to Tracks
          </Link>
        </Button>
        <TrackDetailContent track={track} />
      </main>
    </div>
  );
}

function TrackDetailContent({ track }: { track: Track }) {
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
    <Card className="overflow-hidden border-border/50 bg-card/80 backdrop-blur-sm">
      <CardContent className="p-0">
        <div className="flex flex-col md:flex-row">
          {/* Cover Art */}
          <div className="relative w-full md:w-72 aspect-square md:aspect-auto md:h-auto flex-shrink-0 bg-secondary">
            {!imgError && coverUrl ? (
              <img
                src={coverUrl}
                alt={`Album cover for ${track.albumName}`}
                className="w-full h-full object-cover"
                onError={() => setImgError(true)}
              />
            ) : (
              <div className="w-full h-full min-h-48 flex flex-col items-center justify-center gap-2 text-muted-foreground">
                <Disc3 className="w-12 h-12" />
                <span className="text-xs">No cover available</span>
              </div>
            )}
          </div>

          {/* Track Info */}
          <div className="flex-1 p-6 flex flex-col justify-center gap-5">
            <div className="flex flex-col gap-2">
              <div className="flex items-center gap-2 flex-wrap">
                <span className="font-mono text-xs text-primary bg-primary/10 px-2 py-0.5 rounded">
                  {track.isrCode}
                </span>
                {track.isExplicit && (
                  <Badge variant="destructive" className="text-xs gap-1">
                    <AlertTriangle className="w-3 h-3" />
                    Explicit
                  </Badge>
                )}
              </div>
              <h2 className="text-2xl font-bold tracking-tight text-foreground text-balance">
                {track.name}
              </h2>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <MetadataItem
                icon={<Mic2 className="w-4 h-4" />}
                label="Artist"
                value={track.artistName}
              />
              <MetadataItem
                icon={<Album className="w-4 h-4" />}
                label="Album"
                value={track.albumName}
              />
              <MetadataItem
                icon={<Clock className="w-4 h-4" />}
                label="Duration"
                value={formatDuration(track.playbackSeconds)}
              />
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}

function MetadataItem({
  icon,
  label,
  value,
}: {
  icon: React.ReactNode;
  label: string;
  value: string;
}) {
  return (
    <div className="flex items-start gap-3 p-3 rounded-lg bg-secondary/30">
      <div className="text-primary mt-0.5">{icon}</div>
      <div className="flex flex-col min-w-0">
        <span className="text-xs text-muted-foreground font-medium">
          {label}
        </span>
        <span className="text-sm font-medium text-foreground truncate">
          {value}
        </span>
      </div>
    </div>
  );
}
