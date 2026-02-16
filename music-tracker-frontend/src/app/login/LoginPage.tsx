import { useNavigate } from "react-router";
import { useAppDispatch } from "../config/hooks";
import { useState, type ComponentProps } from "react";
import { loginAction } from "../config/authSlice";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "../../shared/components/cards/card";
import { Label } from "../../shared/components/labels/label";
import { Input } from "../../shared/components/inputs/input";
import { Eye, EyeOff, Loader2, LogIn, Music2 } from "lucide-react";
import { Button } from "../../shared/components/buttons/button";

const BASE_URL = (import.meta.env.VITE_BACKEND_API ?? "") + "/api/v1";

const LoginPage = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsloading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit: ComponentProps<"form">["onSubmit"] = async (e) => {
    e.preventDefault();
    setError("");

    if (!username.trim()) {
      setError("Username is required.");
      return;
    }
    if (!password.trim()) {
      setError("Password is required.");
      return;
    }

    setIsloading(true);

    try {
      const authHeader = `Basic ${btoa(`${username}:${password}`)}`;

      const response = await fetch(`${BASE_URL}/tracks`, {
        method: "GET",
        headers: {
          Authorization: authHeader,
          Accept: "application/json",
        },
      });

      // 401 / 403 means bad credentials
      if (response.status === 401 || response.status === 403) {
        setError("Invalid username or password. Please try again.");
        setIsloading(false);
        return;
      }
      console.log("Logged in successfully!");
      dispatch(loginAction({ username, password }));
      navigate("/tracks");
    } catch (error) {
      console.log(
        "Cannot connect to the server. Please check your connection.",
        error,
      );
      setError("Cannot connect to the server. Please check your connection.");
    } finally {
      console.log("Setting isLoading to false");
      setIsloading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4 py-12">
      <Card className="w-full max-w-md border-border/50 bg-card/80 backdrop-blur-sm">
        <CardHeader className="text-center flex flex-col items-center gap-4 pb-2">
          <div className="flex items-center justify-center w-14 h-14 rounded-2xl bg-primary/10 text-primary">
            <Music2 className="w-7 h-7" />
          </div>
          <div className="flex flex-col gap-1">
            <CardTitle className="text-2xl font-bold tracking-tight">
              Music Tracker
            </CardTitle>
            <CardDescription>
              Sign in with your backend credentials to continue.
            </CardDescription>
          </div>
        </CardHeader>

        <CardContent className="pt-4">
          <form onSubmit={handleSubmit} className="flex flex-col gap-5">
            {/* Error message */}
            {error && (
              <div className="rounded-lg bg-destructive/10 border border-destructive/20 px-4 py-3 text-sm text-destructive">
                {error}
              </div>
            )}

            {/* Username */}
            <div className="flex flex-col gap-2">
              <Label htmlFor="username" className="text-sm font-medium">
                Username
              </Label>
              <Input
                id="username"
                type="text"
                placeholder="Enter your username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                autoComplete="username"
                autoFocus
                disabled={isLoading}
              />
            </div>

            {/* Password */}
            <div className="flex flex-col gap-2">
              <Label htmlFor="password" className="text-sm font-medium">
                Password
              </Label>
              <div className="relative">
                <Input
                  id="password"
                  type={showPassword ? "text" : "password"}
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  autoComplete="current-password"
                  disabled={isLoading}
                  className="pr-10"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                  aria-label={showPassword ? "Hide password" : "Show password"}
                >
                  {showPassword ? (
                    <EyeOff className="w-4 h-4" />
                  ) : (
                    <Eye className="w-4 h-4" />
                  )}
                </button>
              </div>
            </div>

            {/* Submit */}
            <Button type="submit" disabled={isLoading} className="w-full gap-2">
              {isLoading ? (
                <>
                  <Loader2 className="w-4 h-4 animate-spin" />
                  Signing in...
                </>
              ) : (
                <>
                  <LogIn className="w-4 h-4" />
                  Sign In
                </>
              )}
            </Button>

            <p className="text-xs text-center text-muted-foreground">
              Uses HTTP Basic Authentication to connect to the backend API.
            </p>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default LoginPage;
