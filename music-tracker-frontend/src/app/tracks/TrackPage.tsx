import { useNavigate } from "react-router";
import { Plus, Search } from "lucide-react";
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from "../../shared/components/tabs/tabs";
import { useAppDispatch } from "../config/hooks";
import { setTrack } from "./reducer/trackSlice";
import { TrackTab } from "./form/TrackTab";
import {
  useRegisterTrackMutation,
  useLazyGetTrackByIsrCodeQuery,
  useLazyGetTrackCoverQuery,
} from "./service/trackService";

const TrackPage = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [registerTrack] = useRegisterTrackMutation();
  const [getTrackByIsrCode] = useLazyGetTrackByIsrCodeQuery();
  const [getCover] = useLazyGetTrackCoverQuery();

  const handleRegister = (isrCode: string) =>
    registerTrack({ isrCode })
      .unwrap()
      .then(async (track) => {
        dispatch(setTrack(track));
        await getCover(track.isrCode).unwrap().catch(() => {});
        navigate(`/tracks/${encodeURIComponent(track.isrCode)}`);
        return track;
      });

  const handleLookup = (isrCode: string) => getTrackByIsrCode(isrCode).unwrap();

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

          <TabsContent value="register" className="mt-6">
            <TrackTab
              title="Register a New Track"
              description="Fetch track metadata from the music provider and store it locally for future lookups."
              onSearch={handleRegister}
            />
          </TabsContent>

          <TabsContent value="lookup" className="mt-6">
            <TrackTab
              title="Lookup Stored Track"
              description="Retrieve metadata for a track that has already been registered."
              onSearch={handleLookup}
            />
          </TabsContent>
        </Tabs>
      </main>
    </div>
  );
};

export default TrackPage;
