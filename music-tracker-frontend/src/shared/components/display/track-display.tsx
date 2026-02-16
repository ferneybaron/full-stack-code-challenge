import { useState, useEffect } from "react";
import { Clock, Disc3, Mic2, Album, AlertTriangle } from "lucide-react";
import type { Track } from "../../../app/tracks/model/Track";
import { useGetTrackCoverQuery } from "../../../app/tracks/service/trackService";
import { Card, CardContent } from "../cards/card";
import { Badge } from "../badges/badge";

interface TrackDisplayProps {
  track: Track;
}

export function TrackDisplay({ track }: TrackDisplayProps) {
  const [imgError, setImgError] = useState(false);
  const { data: coverBlob } = useGetTrackCoverQuery(track.isrCode, {
    skip: !track.isrCode,
  });
  const [coverUrl] = useState<string>("");

  useEffect(() => {
    if (!coverBlob) {
      return;
    }
    const url = URL.createObjectURL(coverBlob);
    return () => URL.revokeObjectURL(url);
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
            {!imgError ? (
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
