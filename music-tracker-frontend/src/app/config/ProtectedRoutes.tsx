import { Navigate, Outlet } from "react-router-dom";
import { useAppSelector } from "./hooks";
import { selectAuth } from "./authSlice";
import Dashboard from "../../shared/components/layout/Dashboard";

export const ProtectedRoutes = () => {
  const auth = useAppSelector(selectAuth);
  console.log("ProtectedRoutes isAuthenticated: ", auth?.isAuthenticated);

  return auth && auth?.isAuthenticated ? (
    <Dashboard>
      <Outlet />
    </Dashboard>
  ) : (
    <Navigate to="/" />
  );
};
