import { useNavigate } from "react-router";
import { useAppDispatch } from "../config/hooks";
import { useState, type ComponentProps } from "react";
import { loginAction } from "../config/authSlice";

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
    <div className="rounded-lg border bg-card text-card-foreground shadow-sm max-w-md w-full">
      <div className="flex flex-col space-y-1.5 p-6 text-center items-center">
        <h3 className="text-2xl font-semibold leading-none tracking-tight">
          Music Tracker
        </h3>

        <p className="text-sm text-muted-foreground">
          Sign in with your backend credentials to continue.
        </p>
      </div>

      <div className="p-6 pt-0">
        <form onSubmit={handleSubmit} className="flex flex-col gap-5">
          {/* Error message */}
          {error && (
            <div className="rounded-lg bg-destructive/10 border border-destructive/20 px-4 py-3 text-sm text-destructive">
              {error}
            </div>
          )}

          {/* Username */}
          <div className="flex flex-col gap-2">
            <label htmlFor="username" className="text-sm font-medium">
              Username
            </label>
            <input
              id="username"
              type="text"
              placeholder="Enter your username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoComplete="username"
              autoFocus
              disabled={isLoading}
              // Replaced <Input /> with standard HTML + base Tailwind classes
              className="flex h-10 w-full rounded-md border border-input bg-transparent px-3 py-2 text-sm placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
            />
          </div>

          {/* Password */}
          <div className="flex flex-col gap-2">
            <label htmlFor="password" className="text-sm font-medium">
              Password
            </label>
            <div className="relative">
              <input
                id="password"
                type={showPassword ? "text" : "password"}
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                autoComplete="current-password"
                disabled={isLoading}
                // Base Tailwind classes + 'pr-10' to leave room for the eye icon
                className="flex h-10 w-full rounded-md border border-input bg-transparent px-3 py-2 text-sm placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 pr-10"
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors focus:outline-none"
                aria-label={showPassword ? "Hide password" : "Show password"}
              ></button>
            </div>
          </div>

          {/* Submit */}
          <button
            type="submit"
            disabled={isLoading}
            // Replaced <Button /> with standard HTML + base Tailwind classes
            className="inline-flex h-10 w-full items-center justify-center gap-2 rounded-md bg-primary px-4 py-2 text-sm font-medium text-primary-foreground hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 transition-colors"
          >
            {isLoading ? <>Signing in...</> : <>Sign In</>}
          </button>

          <p className="text-xs text-center text-muted-foreground">
            Uses HTTP Basic Authentication to connect to the backend API.
          </p>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
