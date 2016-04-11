package org.vaadin.samples.cafetycoon.domain;

import java.time.Clock;

public final class ClockProvider {

    private ClockProvider() {
    }

    public static Clock getClock() {
        return Clock.systemDefaultZone();
    }
}
