package com.snapp.billsplitter.core.domain;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public final class Event {

    private final String id;


    public Event(String id) {
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}