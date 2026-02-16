import { Plus, Search } from "lucide-react";
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from "../../shared/components/tabs/tabs";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "../../shared/components/cards/card";
import { SearchForm } from "../../shared/components/search/search-form";
import { useState } from "react";
import { TrackSkeleton } from "../../shared/components/skeletons/track-skeleton";
import { TrackList } from "../../shared/components/lists/track-list";

const TrackPage = () => {
  const [isLoading] = useState(false);
  async function handleRegisterTrack(isrCode: string) {
    console.log("handleRegisterTrack isrCode", isrCode);
  }

  async function handleSearchTrack(isrCode: string) {
    console.log("handleSearchTrack isrCode", isrCode);
  }

  return (
    <div className="min-h-screen flex flex-col">
      <main className="flex-1 mx-auto max-w-5xl w-full px-4 sm:px-6 py-8 flex flex-col gap-8">
        {/* Hero Section */}
        <section className="flex flex-col gap-2 text-center py-6">
          <h2 className="text-3xl sm:text-4xl font-bold tracking-tight text-foreground text-balance">
            Track Metadata Lookup
          </h2>
          <p className="text-muted-foreground max-w-xl mx-auto text-balance">
            Search for music tracks using their ISRC codes. Register new tracks
            from the music provider or look up previously stored metadata.
          </p>
        </section>

        {/* Tabs Section */}
        <Tabs defaultValue="register" className="w-full">
          <TabsList className="w-full grid grid-cols-2 h-12">
            <TabsTrigger
              value="register"
              className="gap-2 data-[state=active]:text-primary"
            >
              <Plus className="w-4 h-4" />
              Register Track
            </TabsTrigger>
            <TabsTrigger
              value="lookup"
              className="gap-2 data-[state=active]:text-primary"
            >
              <Search className="w-4 h-4" />
              Lookup Track
            </TabsTrigger>
          </TabsList>

          {/* Register Tab */}
          <TabsContent value="register" className="mt-6 flex flex-col gap-6">
            <Card className="border-border/50 bg-card/80 backdrop-blur-sm">
              <CardHeader>
                <CardTitle className="text-lg">Register a New Track</CardTitle>
                <CardDescription>
                  Fetch track metadata from the music provider and store it
                  locally for future lookups.
                </CardDescription>
              </CardHeader>
              <CardContent>
                <SearchForm
                  onSubmit={handleRegisterTrack}
                  isLoading={isLoading}
                  label="ISRC Code"
                  placeholder="e.g. USRC17607839"
                />
              </CardContent>
            </Card>

            {isLoading && <TrackSkeleton />}
          </TabsContent>

          {/* Lookup Tab */}
          <TabsContent value="lookup" className="mt-6 flex flex-col gap-6">
            <Card className="border-border/50 bg-card/80 backdrop-blur-sm">
              <CardHeader>
                <CardTitle className="text-lg">Lookup Stored Track</CardTitle>
                <CardDescription>
                  Retrieve metadata for a track that has already been
                  registered. Results appear in the list below -- click any
                  track to view full details.
                </CardDescription>
              </CardHeader>
              <CardContent>
                <SearchForm
                  onSubmit={handleSearchTrack}
                  isLoading={isLoading}
                  label="ISRC Code"
                  placeholder="e.g. USRC17607839"
                />
              </CardContent>
            </Card>

            <TrackList tracks={[]} />
          </TabsContent>
        </Tabs>
      </main>
    </div>
  );
};

export default TrackPage;
