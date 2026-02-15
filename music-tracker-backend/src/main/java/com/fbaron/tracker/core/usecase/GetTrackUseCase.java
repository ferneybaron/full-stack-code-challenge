package com.fbaron.tracker.core.usecase;

import com.fbaron.tracker.core.model.Track;

public interface GetTrackUseCase {

    Track getTrackByIsrCode(String isrCode);

}
