package org.vaadin.samples.cafetycoon.domain;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseEntity implements Serializable {

    private final long id;

    protected BaseEntity() {
        this.id = nextId.getAndIncrement();
    }

    protected BaseEntity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseEntity that = (BaseEntity) o;

        if (id != that.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return String.format("BaseEntity{id=%d}", id);
    }

    private static final AtomicLong nextId = new AtomicLong(1);
}
