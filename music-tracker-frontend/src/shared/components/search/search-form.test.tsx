import { describe, it, expect, vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import { SearchForm } from "./search-form";

describe("SearchForm", () => {
  it("renders label and input", () => {
    const onSubmit = vi.fn();
    render(
      <SearchForm
        onSubmit={onSubmit}
        isLoading={false}
        label="ISRC Code"
        placeholder="e.g. USMC18620549"
      />,
    );
    expect(screen.getByLabelText(/ISRC Code/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/USMC18620549/)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /Search/i })).toBeInTheDocument();
  });

  it("calls onSubmit with trimmed uppercase ISRC on submit", async () => {
    const onSubmit = vi.fn();
    render(
      <SearchForm
        onSubmit={onSubmit}
        isLoading={false}
        label="ISRC Code"
      />,
    );
    const input = screen.getByLabelText(/ISRC Code/i);
    fireEvent.change(input, { target: { value: "  usmc18620549  " } });
    fireEvent.click(screen.getByRole("button", { name: /Search/i }));
    expect(onSubmit).toHaveBeenCalledTimes(1);
    expect(onSubmit).toHaveBeenCalledWith("USMC18620549");
  });

  it("does not call onSubmit when input is empty", () => {
    const onSubmit = vi.fn();
    render(
      <SearchForm
        onSubmit={onSubmit}
        isLoading={false}
        label="ISRC Code"
      />,
    );
    fireEvent.click(screen.getByRole("button", { name: /Search/i }));
    expect(onSubmit).not.toHaveBeenCalled();
  });

  it("disables button when isLoading", () => {
    const onSubmit = vi.fn();
    render(
      <SearchForm
        onSubmit={onSubmit}
        isLoading={true}
        label="ISRC Code"
      />,
    );
    expect(screen.getByRole("button", { name: /Search/i })).toBeDisabled();
  });
});
