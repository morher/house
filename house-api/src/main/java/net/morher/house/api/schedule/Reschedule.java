package net.morher.house.api.schedule;

import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Reschedule extends Exception {
    private static final long serialVersionUID = 1L;
    private final Instant rescheduleAt;

    public static Reschedule at(Instant instant) {
        return new Reschedule(instant);
    }

    public static Reschedule now() {
        return new Reschedule(null);
    }
}
