package net.morher.house.api.schedule;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DelayedTrigger {
    private final Runnable scheduleCallback = this::considerRun;
    private final HouseScheduler scheduler;
    private final ScheduledRunnable task;
    private Instant nextExecution;

    public DelayedTrigger(HouseScheduler scheduler, ScheduledRunnable task) {
        this.scheduler = scheduler;
        this.task = task;
    }

    @Synchronized
    public void cancel() {
        nextExecution = null;
    }

    @Synchronized
    public void runAt(Instant time) {
        if (time == null) {
            time = scheduler.now();
        }
        log.trace("Update next execution of task '{}' from {} to {}", task, nextExecution, time);
        nextExecution = time;
        scheduler.execute(scheduleCallback);
    }

    public void runAfter(Duration duration) {
        runAt(scheduler.now().plus(duration));
    }

    public void runAfter(long delay, TemporalUnit unit) {
        runAt(scheduler.now().plus(delay, unit));
    }

    public void runNow() {
        runAt(scheduler.now());
    }

    @Synchronized
    private void considerRun() {
        if (nextExecution == null) {
            log.trace("No planned execution of task '{}'", task);
            return;
        }
        Instant now = scheduler.now();
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
