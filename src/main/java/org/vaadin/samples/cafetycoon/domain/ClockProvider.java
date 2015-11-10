package org.vaadin.samples.cafetycoon.domain;

import java.time.Clock;

/**
 * Created by petterwork on 10/11/15.
 */
public final class ClockProvider {

    private ClockProvider() {
    }

    public static Clock getClock() {
        return Clock.systemDefaultZone();
    }
}
