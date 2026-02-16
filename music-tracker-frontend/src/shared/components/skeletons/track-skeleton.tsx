import { Card, CardContent } from "../cards/card";
import { Skeleton } from "./skeleton";

export function TrackSkeleton() {
  return (
    <Card className="overflow-hidden border-border/50 bg-card/80">
      <CardContent className="p-0">
        <div className="flex flex-col md:flex-row">
          <Skeleton className="w-full md:w-72 aspect-square md:aspect-auto md:h-64 flex-shrink-0" />
          <div className="flex-1 p-6 flex flex-col justify-center gap-5">
            <div className="flex flex-col gap-2">
              <Skeleton className="h-5 w-32" />
              <Skeleton className="h-8 w-3/4" />
            </div>
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <Skeleton className="h-16 rounded-lg" />
              <Skeleton className="h-16 rounded-lg" />
              <Skeleton className="h-16 rounded-lg" />
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
