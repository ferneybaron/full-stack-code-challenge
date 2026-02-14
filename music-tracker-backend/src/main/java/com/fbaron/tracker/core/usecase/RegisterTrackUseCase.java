package com.fbaron.tracker.core.usecase;

import com.fbaron.tracker.core.model.RegistrationResult;

public interface RegisterTrackUseCase {

    RegistrationResult register(String isrCode);

}
