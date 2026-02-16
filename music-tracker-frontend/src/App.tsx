import { BrowserRouter, Route, Routes } from "react-router";
import "./App.css";
import LoginPage from "./app/login/LoginPage";
import TrackPage from "./app/tracks/TrackPage";

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/tracks" element={<TrackPage />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
