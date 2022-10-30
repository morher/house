package net.morher.house.api.schedule;

import java.time.Instant;

import lombok.Data;

@Data
public class Reschedule extends Exception {
    private static final long serialVersionUID = 1L;
    private final Instant rescheduleAt;

    public static Reschedule at(Instant instant) {
        return new Reschedule(instant);
    }

    public static Reschedule now() {
        return new Reschedule(Instant.now());
    }
}
