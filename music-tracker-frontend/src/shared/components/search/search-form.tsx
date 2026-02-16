import { useState } from "react";
import { Search, Loader2 } from "lucide-react";
import { Input } from "../inputs/input";
import { Button } from "../buttons/button";

interface SearchFormProps {
  onSubmit: (isrcCode: string) => void;
  isLoading: boolean;
  label: string;
  placeholder?: string;
}

export function SearchForm({
  onSubmit,
  isLoading,
  label,
  placeholder = "e.g. USRC17607839",
}: SearchFormProps) {
  const [isrcCode, setIsrcCode] = useState("");

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const trimmed = isrcCode.trim().toUpperCase();
    if (!trimmed) return;
    onSubmit(trimmed);
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-3">
      <label
        htmlFor="isrc-input"
        className="text-sm font-medium text-foreground"
      >
        {label}
      </label>
      <div className="flex flex-col gap-2 sm:flex-row">
        <div className="relative flex-1 min-w-0">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <Input
            id="isrc-input"
            type="text"
            value={isrcCode}
            onChange={(e) => setIsrcCode(e.target.value)}
            placeholder={placeholder}
            className="pl-9 font-mono text-sm h-11 bg-secondary/50 border-border/50 focus:border-primary/50"
            disabled={isLoading}
            aria-label="ISRC Code"
          />
        </div>
        <Button
          type="submit"
          disabled={isLoading || !isrcCode.trim()}
          className="h-11 px-6 font-medium w-full sm:w-auto"
        >
          {isLoading ? (
            <>
              <Loader2 className="w-4 h-4 animate-spin" />
              <span className="sr-only">Searching...</span>
            </>
          ) : (
            "Search"
          )}
        </Button>
      </div>
      <p className="text-xs text-muted-foreground">
        Enter a valid ISRC code (International Standard Recording Code) to look
        up track metadata.
      </p>
    </form>
  );
}
