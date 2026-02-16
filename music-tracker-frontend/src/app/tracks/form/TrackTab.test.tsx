import { describe, it, expect, vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import { Provider } from "react-redux";
import { MemoryRouter } from "react-router";
import { store } from "../../config/store";
import { TrackTab } from "./TrackTab";
import type { Track } from "../model/Track";

function renderWithRouter(ui: React.ReactElement) {
  return render(
    <Provider store={store}>
      <MemoryRouter>{ui}</MemoryRouter>
    </Provider>,
  );
}

const mockTrack: Track = {
  isrCode: "USMC18620549",
  name: "Test Track",
  artistName: "Artist",
  albumName: "Album",
  albumId: "album-1",
  isExplicit: false,
  playbackSeconds: 180,
  coverPath: null,
};

describe("TrackTab", () => {
  it("renders title and description", () => {
    const onSearch = vi.fn().mockResolvedValue(null as unknown as Track);
    renderWithRouter(
      <TrackTab
        title="Register Track"
        description="Fetch track metadata."
        onSearch={onSearch}
      />,
    );
    expect(screen.getByText("Register Track")).toBeInTheDocument();
    expect(screen.getByText("Fetch track metadata.")).toBeInTheDocument();
  });

  it("shows SearchForm and calls onSearch with ISRC on submit", async () => {
    const onSearch = vi.fn().mockResolvedValue(mockTrack);
    renderWithRouter(
      <TrackTab
        title="Register"
        description="Register a track."
        onSearch={onSearch}
      />,
    );
    fireEvent.change(screen.getByLabelText(/ISRC Code/i), {
      target: { value: "USMC18620549" },
    });
    fireEvent.click(screen.getByRole("button", { name: /Search/i }));
    expect(onSearch).toHaveBeenCalledWith("USMC18620549");
  });

  it("shows track list when onSearch resolves with a track", async () => {
    const onSearch = vi.fn().mockResolvedValue(mockTrack);
    renderWithRouter(
      <TrackTab
        title="Lookup"
        description="Lookup a track."
        onSearch={onSearch}
      />,
    );
    fireEvent.change(screen.getByLabelText(/ISRC Code/i), {
      target: { value: "USMC18620549" },
    });
    fireEvent.click(screen.getByRole("button", { name: /Search/i }));
    expect(await screen.findByText("Test Track")).toBeInTheDocument();
    expect(screen.getByText(/1 result/)).toBeInTheDocument();
  });

  it("shows error when onSearch rejects", async () => {
    const onSearch = vi.fn().mockRejectedValue(new Error("Track not found"));
    renderWithRouter(
      <TrackTab
        title="Lookup"
        description="Lookup a track."
        onSearch={onSearch}
      />,
    );
    fireEvent.change(screen.getByLabelText(/ISRC Code/i), {
      target: { value: "BADCODE" },
    });
    fireEvent.click(screen.getByRole("button", { name: /Search/i }));
    expect(await screen.findByText(/Track not found|Something went wrong/)).toBeInTheDocument();
  });
});
