package net.morher.house.test.schedule;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import net.morher.house.api.schedule.HouseScheduler;

/**
 * <p>
 * An implementation of {@link HouseScheduler} that helps testing scheduled components. The implementation simulates time and
 * schedules tasks to be run in this time space.
 * 
 * <p>
 * The TestHouseScheduler lets the test code control the simulation of time progression through {@link #skipTo(Instant)},
 * {@link #skipAhead(Duration)}, {@link TestHouseScheduler#skipAhead(long, TemporalUnit)}, {@link #skipAhead(long, TimeUnit)}
 * and {@link #skipAheadToLastRegisteredTaskAndRunAllWaiting()}. Whenever time is skipped forward all tasks that are scheduled
 * to run in this time frame are run. If any task schedules new tasks to be run within this time frame, they are run as well.
 * All tasks are run in the chronological order. Tasks that are scheduled to be run at the new current time are not run by the
 * skip-methods, except {@link #skipAheadToLastRegisteredTaskAndRunAllWaiting()}. To run tasks scheduled for "now", call
 * {@link #runWaitingTasks()}.
 * 
 * <p>
 * The current time can be retrieved by calling {@link #now()}. Notice that the code being tested should also call
 * {@link HouseScheduler#now()} to get the current time, and not {@link Instant#now()}. Failing to do so will give unexpected
 * results as two different time spaces will be used.
 * 
 * <p>
 * {@link HouseScheduler#get()} should return this implementation when it is found on the classpath.
 * {@link TestHouseScheduler#get()} is provided as a convenience method for verifying and casting.
 * 
 * @author Morten Hermansen
 */
public class TestHouseScheduler implements HouseScheduler {
    private static final AtomicLong sequencer = new AtomicLong();
    private Instant now;
    private Task<?> nextTask;

    /**
     * Get the global instance of {@link HouseScheduler} and verify the implementation is TestHouseScheduler.
     * 
     * @return Returns the global scheduler instance as TestHouseScheduler.
     */
    public static TestHouseScheduler get() {
        HouseScheduler scheduler = HouseScheduler.get();
        if (scheduler instanceof TestHouseScheduler) {
            return (TestHouseScheduler) scheduler;
        }
        throw new IllegalArgumentException("TestHouseScheduler is not the current implementation");
    }

    /**
     * Create a TestHouseScheduler starting at the current time.
     */
    public TestHouseScheduler() {
        this(Instant.now());
    }

    /**
     * Create a TestHouseScheduler starting at a given time.
     * 
     * @param now
     *            The initial time for the scheduler.
     */
    public TestHouseScheduler(Instant now) {
        this.now = now;
    }

    /**
     * Skip ahead a given amount of time and run scheduled tasks due before the new now. It is only possible to skip forwards in
     * time.
     * 
     * @param amount
     *            The amount of time
     * @param unit
     *            The unit for the amount of time
     * @return Itself
     */
    public TestHouseScheduler skipAhead(long amount, TimeUnit unit) {
        return skipAhead(Duration.ofNanos(unit.toNanos(amount)));
    }

    /**
     * Skip ahead a given amount of time and run scheduled tasks due before the new now. It is only possible to skip forwards in
     * time.
     * 
     * @param amount
     *            The amount of time
     * @param unit
     *            The unit for the amount of time
     * @return Itself
     */
    public TestHouseScheduler skipAhead(long amount, TemporalUnit unit) {
        return skipAhead(Duration.of(amount, unit));
    }

    /**
     * Skip ahead a given amount of time and run scheduled tasks due before the new now. It is only possible to skip forwards in
     * time.
     * 
     * @param duration
     *            The amount of time to skip
     * @return Itself
     */
    public TestHouseScheduler skipAhead(Duration duration) {
        return skipTo(now.plus(duration));
    }

    /**
     * Skip ahead to a given moment in time and run scheduled tasks due before the new now. It is only possible to skip forwards
     * in time.
     * 
     * @param skipTo
     *            The moment to skip to
     * @return Itself
     */
    public TestHouseScheduler skipTo(Instant skipTo) {
        if (skipTo.isBefore(now)) {
            throw new IllegalArgumentException("Can only go forwards in time...");
        }
        while (nextTask != null && nextTask.getRunAt().isBefore(skipTo)) {
            Instant taskTime = nextTask.getRunAt();
            if (taskTime.isAfter(now)) {
                now = taskTime;
            }
            runWaitingTasks();
        }
        now = skipTo;
        return this;
    }

    /**
     * Run all tasks that are due by "now".
     */
    public void runWaitingTasks() {
        while (isNextTaskReady()) {
            Task<?> task = shiftNextTask();
            task.runAndConsiderRescheduling()
                    .ifPresent(this::schedule);
        }
    }

    /**
     * Skip ahead to the time the last scheduled task is due to run and run all tasks in between and at this time. Keep in mind
     * that this includes new tasks scheduled by tasks being run. New tasks that are scheduled to be run after the time skipped
     * ahead to will not be run. Notice that the skip-ahead is calculated before running the tasks, and new scheduling will not
     * affect the skip-ahead.
     */
    public void skipAheadToLastRegisteredTaskAndRunAllWaiting() {
        Task<?> task = getLastTask();
        if (task != null && task.getRunAt().isAfter(now)) {
            skipTo(task.getRunAt());
        }
        runWaitingTasks();
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return schedule(new Task<>(command, now.plusNanos(unit.toNanos(delay)), null));
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return schedule(new Task<>(callable, now.plusNanos(unit.toNanos(delay))));
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return schedule(new Task<>(command, now.plusNanos(unit.toNanos(initialDelay)), Duration.ofNanos(unit.toNanos(period)), false));
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return schedule(new Task<>(command, now.plusNanos(unit.toNanos(initialDelay)), Duration.ofNanos(unit.toNanos(delay)), true));
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("scheduleWithFixedDelay not implemented in TestHouseScheduler");
    }

    @Override
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException("shutdown not implemented in TestHouseScheduler");
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("awaitTermination not implemented in TestHouseScheduler");
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return new Task<>(task, now);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return new Task<>(task, now, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return new Task<>(task, now, null);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new UnsupportedOperationException("invokeAll not implemented in TestHouseScheduler");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("invokeAll not implemented in TestHouseScheduler");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("invokeAny not implemented in TestHouseScheduler");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("invokeAny not implemented in TestHouseScheduler");
    }

    @Override
    public void execute(Runnable command) {
        schedule(new Task<>(command, now, null));
    }

    @Synchronized
    private <S> Task<S> schedule(Task<S> task) {
        if (nextTask == null || nextTask.runsAfter(task)) {
            task.setNextTask(nextTask);
            nextTask = task;
        } else {
            Task<?> precedingTask = nextTask;
            while (precedingTask.getNextTask() != null) {
                if (precedingTask.getNextTask().runsAfter(task)) {
                    break;
                }
                precedingTask = precedingTask.getNextTask();
            }
            task.setNextTask(precedingTask.getNextTask());
            precedingTask.setNextTask(task);
        }
        return task;
    }

    @Synchronized
    private Task<?> getLastTask() {
        Task<?> task = null;
        Task<?> nextTask = this.nextTask;
        while (nextTask != null) {
            task = nextTask;
            nextTask = task.getNextTask();
        }
        return task;
    }

    private boolean isNextTaskReady() {
        return nextTask != null
                && !nextTask.getRunAt().isAfter(now);
    }

    @Synchronized
    private Task<?> shiftNextTask() {
        Task<?> task = nextTask;
        if (task != null) {
            nextTask = task.getNextTask();
        }
        return task;
    }

    @Override
    public Instant now() {
        return now;
    }

    private class Task<T> implements ScheduledFuture<T> {
        private final long sequenceNumber = sequencer.getAndIncrement();
        private final CompletableFuture<T> delegate;
        private final Runnable callback;
        private final Callable<T> callable;
        @Getter
        private final Instant runAt;
        private final Duration rescheduleDelay;
        private final boolean rescheduleRelativeToCompletion;
        @Getter
        @Setter
        private Task<?> nextTask;
        private Exception exception;
        private T result;

        private Task(CompletableFuture<T> delegate, Runnable callback, Callable<T> callable, Instant runAt, Duration rescheduleDelay, boolean rescheduleRelativeToCompletion, T result) {
            this.delegate = delegate;
            this.callback = callback;
            this.callable = callable;
            this.runAt = runAt;
            this.rescheduleDelay = rescheduleDelay;
            this.rescheduleRelativeToCompletion = rescheduleRelativeToCompletion;
        }

        public Task(Runnable callback, Instant runAt, T result) {
            this(new CompletableFuture<>(), callback, null, runAt, null, false, result);
        }

        public Task(Runnable callback, Instant runAt, Duration rescheduleDelay, boolean rescheduleRelativeToCompletion) {
            this(new CompletableFuture<>(), callback, null, runAt, rescheduleDelay, rescheduleRelativeToCompletion, null);
        }

        public Task(Callable<T> callable, Instant runAt) {
            this(new CompletableFuture<>(), null, callable, runAt, null, false, null);
        }

        public boolean runsAfter(Task<?> task) {
            return runAt.isAfter(task.runAt);
        }

        public Optional<Task<T>> runAndConsiderRescheduling() {
            if (isCancelled()) {
                return Optional.empty();
            }
            if (callback != null) {
                callback.run();
            }
            if (callable != null) {
                try {
                    result = callable.call();
                } catch (Exception e) {
                    exception = e;
                }
            }
            if (rescheduleDelay != null) {
                Instant runNext = rescheduleRelativeToCompletion
                        ? now().plus(rescheduleDelay)
                        : runAt.plus(rescheduleDelay);
                return Optional.of(new Task<T>(delegate, callback, callable, runNext, rescheduleDelay, rescheduleRelativeToCompletion, result));
            }
            if (exception != null) {
                delegate.completeExceptionally(exception);
            } else {
                delegate.complete(result);
            }
            return Optional.empty();
        }

        @Override
        public long getDelay(TimeUnit unit) {
            Instant now = TestHouseScheduler.this.now;
            if (now.isBefore(runAt)) {
                return unit.convert(Duration.between(now, runAt));
            }
            return 0;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return delegate.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return delegate.isCancelled();
        }

        @Override
        public boolean isDone() {
            return delegate.isDone();
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            return delegate.get();
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return delegate.get(timeout, unit);
        }

        @Override
        public int compareTo(Delayed other) {
            if (other == this) // compare zero if same object
                return 0;
            if (other instanceof Task) {
                Task<?> x = (Task<?>) other;
                long diff = runAt.compareTo(x.getRunAt());
                if (diff < 0)
                    return -1;
                else if (diff > 0)
                    return 1;
                else if (sequenceNumber < x.sequenceNumber)
                    return -1;
                else
                    return 1;
            }
            long diff = getDelay(NANOSECONDS) - other.getDelay(NANOSECONDS);
            return (diff < 0) ? -1 : (diff > 0) ? 1 : 0;
        }
    }
}
