export interface Track {
  isrCode: string;
  name: string;
  artistName: string;
  albumName: string;
  albumId: string;
  isExplicit: boolean;
  playbackSeconds: number;
  coverPath: string | null;
}
