import { SearchX } from "lucide-react";
import { Card, CardContent } from "../cards/card";

interface ErrorDisplayProps {
  message: string;
  status?: number;
}

export function ErrorDisplay({ message, status }: ErrorDisplayProps) {
  const title = getTitle(status);

  return (
    <Card className="border-destructive/30 bg-destructive/5">
      <CardContent className="flex items-start gap-4 p-5">
        <div className="flex items-center justify-center w-10 h-10 rounded-lg bg-destructive/10 text-destructive flex-shrink-0">
          <SearchX className="w-5 h-5" />
        </div>
        <div className="flex flex-col gap-1 min-w-0">
          <h3 className="font-semibold text-sm text-destructive">{title}</h3>
          <p className="text-sm text-muted-foreground">{message}</p>
          {status && (
            <span className="text-xs font-mono text-muted-foreground/70 mt-1">
              {"Status: "}
              {status}
            </span>
          )}
        </div>
      </CardContent>
    </Card>
  );
}

function getTitle(status?: number) {
  if (status === 404) return "Track Not Found";
  if (status === 503) return "Service Unavailable";
  return "Something Went Wrong";
}
