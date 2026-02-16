import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "./store";

export interface AuthState {
  usermane: string;
  password: string;
  isAuthenticated: boolean;
}

const initialState: AuthState = {
  usermane: "",
  password: "",
  isAuthenticated: false,
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    loginAction(
      state: AuthState,
      action: PayloadAction<{ username: string; password: string }>,
    ) {
      state.usermane = action.payload.username;
      state.password = action.payload.password;
      state.isAuthenticated = true;
    },
    logoutAction(state: AuthState) {
      state.usermane = "";
      state.password = "";
      state.isAuthenticated = false;
    },
  },
});

export const { loginAction, logoutAction } = authSlice.actions;

export const selectAuth = (state: RootState) => state.auth;

export default authSlice.reducer;
