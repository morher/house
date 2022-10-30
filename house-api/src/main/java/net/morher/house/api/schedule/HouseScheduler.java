package net.morher.house.api.schedule;

import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;

public interface HouseScheduler extends ScheduledExecutorService {

    public static HouseScheduler get() {
        return HouseSchedulerLocator.get();
    }

    public static HouseScheduler get(String name) {
        return HouseSchedulerLocator.get();
    }

    Instant now();

    default DelayedTrigger delayedTrigger(String name, ScheduledRunnable task) {
        return new DelayedTrigger(new NamedTask(task, name), this, this::now);
    }
}
