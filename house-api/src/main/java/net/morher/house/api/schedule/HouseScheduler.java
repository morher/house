package net.morher.house.api.schedule;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

import net.morher.house.api.state.DelayedStateChange;

public interface HouseScheduler extends ScheduledExecutorService {

    public static HouseScheduler get() {
        return HouseSchedulerLocator.get();
    }

    @Deprecated
    public static HouseScheduler get(String name) {
        return HouseSchedulerLocator.get();
    }

    Instant now();

    default DelayedTrigger delayedTrigger(String name, ScheduledRunnable task) {
        return new DelayedTrigger(this, new NamedTask(task, name));
    }

    default <T> DelayedStateChange<T> delayedStateChange(Consumer<? super T> listener, Duration defaultDelay) {
        return new DelayedStateChange<>(this, listener, defaultDelay);
    }
}
