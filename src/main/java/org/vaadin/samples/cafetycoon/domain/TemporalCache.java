package org.vaadin.samples.cafetycoon.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TemporalCache<E extends TemporalCache.Entry> {

    // This is a quick-and-dirty implementation not suitable for production use.

    private List<E> entries = new ArrayList<>();

    private Duration duration = Duration.ofDays(1);

    public void setCacheDuration(Duration duration) {
        this.duration = duration;
        removeOldEntries();
    }

    public void add(E entry) {
        removeOldEntries();
        entries.add(entry);
        Collections.sort(entries);
    }

    public List<E> getEntries() {
        removeOldEntries();
        return Collections.unmodifiableList(entries);
    }

    private void removeOldEntries() {
        Instant threshold = ClockProvider.getClock().instant().minus(duration);
        Iterator<E> it = entries.iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (e.getInstant().isBefore(threshold)) {
                it.remove();
            } else {
                return;
            }
        }
    }

    public interface Entry extends Comparable<Entry> {
        Instant getInstant();

        @Override
        default int compareTo(Entry o) {
            return getInstant().compareTo(o.getInstant());
        }
    }
}
