import { BrowserRouter, Route, Routes } from "react-router";
import "./App.css";
import LoginPage from "./app/login/LoginPage";
import TrackPage from "./app/tracks/TrackPage";
import { ProtectedRoutes } from "./app/config/ProtectedRoutes";
import { TrackDetail } from "./app/tracks/details/TrackDetail";

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<LoginPage />} />
          {/* Protected Routes */}
          <Route path="/tracks" element={<ProtectedRoutes />}>
            <Route index element={<TrackPage />} />
            <Route path="/tracks/register" element={<TrackPage />} />
            <Route path="/tracks/:id" element={<TrackDetail />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
