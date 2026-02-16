import { useState, useCallback } from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "../../../shared/components/cards/card";
import { SearchForm } from "../../../shared/components/search/search-form";
import { TrackSkeleton } from "../../../shared/components/skeletons/track-skeleton";
import { ErrorDisplay } from "../../../shared/components/errors/error-display";
import type { Track } from "../model/Track";
import { TrackList } from "../../../shared/components/lists/track-list";

export interface TrackTabProps {
  title: string;
  description: string;
  onSearch: (isrCode: string) => Promise<Track>;
}

export function TrackTab({ title, description, onSearch }: TrackTabProps) {
  const [isLoading, setIsLoading] = useState(false);
  const [track, setTrack] = useState<Track | null>(null);
  const [error, setError] = useState<{
    message: string;
    status?: number;
  } | null>(null);

  const handleSubmit = useCallback(
    async (isrCode: string) => {
      setError(null);
      setTrack(null);
      setIsLoading(true);
      try {
        const result = await onSearch(isrCode);
        setTrack(result);
      } catch (err: unknown) {
        const status =
          err && typeof err === "object" && "status" in err
            ? (err as { status?: number }).status
            : undefined;
        let message = "Something went wrong";
        if (err && typeof err === "object" && "data" in err) {
          const data = (err as { data?: unknown }).data;
          if (data && typeof data === "object" && "detail" in data) {
            message = String((data as { detail?: unknown }).detail);
          } else {
            message = String(data);
          }
        } else if (err instanceof Error) {
          message = err.message;
        }
        setError({ message, status });
      } finally {
        setIsLoading(false);
      }
    },
    [onSearch],
  );

  return (
    <div className="flex flex-col gap-6">
      <Card className="border-border/50 bg-card/80 backdrop-blur-sm">
        <CardHeader>
          <CardTitle className="text-lg">{title}</CardTitle>
          <CardDescription>{description}</CardDescription>
        </CardHeader>
        <CardContent>
          <SearchForm
            onSubmit={handleSubmit}
            isLoading={isLoading}
            label="ISRC Code"
            placeholder="e.g. USMC18620549"
          />
        </CardContent>
      </Card>

      {isLoading && <TrackSkeleton />}
      {error && !isLoading && (
        <ErrorDisplay message={error.message} status={error.status} />
      )}
      {track && !isLoading && <TrackList tracks={[track]} />}
    </div>
  );
}
