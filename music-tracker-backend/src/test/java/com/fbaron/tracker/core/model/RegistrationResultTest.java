package com.fbaron.tracker.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegistrationResultTest {

    @Test
    @DisplayName("record returns track and saved as passed")
    void recordAccessors() {
        Track track = Track.builder().isrCode("USMC18620549").name("Track").build();

        RegistrationResult created = new RegistrationResult(track, true);
        assertThat(created.track()).isSameAs(track);
        assertThat(created.saved()).isTrue();

        RegistrationResult existing = new RegistrationResult(track, false);
        assertThat(existing.track()).isSameAs(track);
        assertThat(existing.saved()).isFalse();
    }

    @Test
    @DisplayName("equal when same track and saved value")
    void equals() {
        Track track = Track.builder().isrCode("USMC").name("T").build();
        RegistrationResult a = new RegistrationResult(track, true);
        RegistrationResult b = new RegistrationResult(track, true);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
