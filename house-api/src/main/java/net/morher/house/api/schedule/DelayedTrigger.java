package net.morher.house.api.schedule;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DelayedTrigger {
    private final Runnable scheduleCallback = this::considerRun;
    private final ScheduledRunnable task;
    private final ScheduledExecutorService scheduler;
    private final Supplier<Instant> timeProvider;
    private Instant nextExecution;

    public DelayedTrigger(ScheduledRunnable task, ScheduledExecutorService scheduler, Supplier<Instant> timeProvider) {
        this.task = task;
        this.scheduler = scheduler;
        this.timeProvider = timeProvider;
    }

    public DelayedTrigger(ScheduledRunnable task, ScheduledExecutorService scheduler) {
        this(task, scheduler, Instant::now);
    }

    @Synchronized
    public void cancel() {
        nextExecution = null;
    }

    @Synchronized
    public void runAt(Instant time) {
        log.trace("Update next execution of task '{}' from {} to {}", task, nextExecution, time);
        nextExecution = time;
        scheduler.execute(scheduleCallback);
    }

    public void runAfter(Duration duration) {
        runAt(timeProvider.get().plus(duration));
    }

    public void runAfter(long delay, TemporalUnit unit) {
        runAt(timeProvider.get().plus(delay, unit));
    }

    public void runNow() {
        runAt(timeProvider.get());
    }

    @Synchronized
    private void considerRun() {
        if (nextExecution == null) {
            log.trace("No planned execution of task '{}'", task);
            return;
        }
        Instant now = timeProvider.get();
        if (now.isBefore(nextExecution)) {
            long waitMs = ChronoUnit.MILLIS.between(now, nextExecution);
            log.trace("Next execution time for task '{}' not reached. Schedule new attempt in {} ms", task, waitMs);
            scheduler.schedule(scheduleCallback, waitMs, TimeUnit.MILLISECONDS);
            return;
        }
        log.debug("Executing task '{}'", task);
        nextExecution = null;
        try {
            task.runScheduled();

        } catch (Reschedule r) {
            runAt(r.getRescheduleAt());
        }
    }
}
