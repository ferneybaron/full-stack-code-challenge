import { combineReducers } from "@reduxjs/toolkit";
import authReducer from "./authSlice";
import { trackService } from "../tracks/service/trackService";
import trackReducer from "../tracks/reducer/trackSlice";

const rootReducer = combineReducers({
  auth: authReducer,
  trackState: trackReducer,
  [trackService.reducerPath]: trackService.reducer,
});

export type RootState = ReturnType<typeof rootReducer>;
export default rootReducer;
