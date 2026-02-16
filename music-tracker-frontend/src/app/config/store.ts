import { configureStore } from "@reduxjs/toolkit";
import rootReducer from "./rootReducer";
import { trackService } from "../tracks/service/trackService";

export const store = configureStore({
  reducer: rootReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        // getTrackCover returns a Blob stored in query cache; allow it
        ignoredActions: ["trackService/executeQuery/fulfilled"],
        ignoredPaths: ["trackService.queries"],
      },
    }).concat(trackService.middleware),
});

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>;
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch;
