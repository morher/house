package net.morher.house.api.schedule;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultHouseScheduler implements HouseScheduler {
  private ScheduledExecutorService scheduler;

  public DefaultHouseScheduler() {
    scheduler =
        new ExceptionHandlingSchedulerWrapper(
            Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("House Scheduler")),
            this::handleException);
  }

  private void handleException(Throwable e, Object task) {
    log.error("Execption while performing task '{}':", task, e.getMessage(), e);
  }

  public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
    return scheduler.schedule(command, delay, unit);
  }

  public void execute(Runnable command) {
    scheduler.execute(command);
  }

  public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
    return scheduler.schedule(callable, delay, unit);
  }

  public ScheduledFuture<?> scheduleAtFixedRate(
      Runnable command, long initialDelay, long period, TimeUnit unit) {
    return scheduler.scheduleAtFixedRate(command, initialDelay, period, unit);
  }

  public void shutdown() {
    scheduler.shutdown();
  }

  public List<Runnable> shutdownNow() {
    return scheduler.shutdownNow();
  }

  public boolean isShutdown() {
    return scheduler.isShutdown();
  }

  public ScheduledFuture<?> scheduleWithFixedDelay(
      Runnable command, long initialDelay, long delay, TimeUnit unit) {
    return scheduler.scheduleWithFixedDelay(command, initialDelay, delay, unit);
  }

  public boolean isTerminated() {
    return scheduler.isTerminated();
  }

  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    return scheduler.awaitTermination(timeout, unit);
  }

  public <T> Future<T> submit(Callable<T> task) {
    return scheduler.submit(task);
  }

  public <T> Future<T> submit(Runnable task, T result) {
    return scheduler.submit(task, result);
  }

  public Future<?> submit(Runnable task) {
    return scheduler.submit(task);
  }

  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
      throws InterruptedException {
    return scheduler.invokeAll(tasks);
  }

  public <T> List<Future<T>> invokeAll(
      Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException {
    return scheduler.invokeAll(tasks, timeout, unit);
  }

  public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
      throws InterruptedException, ExecutionException {
    return scheduler.invokeAny(tasks);
  }

  public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    return scheduler.invokeAny(tasks, timeout, unit);
  }

  @Override
  public Instant now() {
    return Instant.now();
  }

  private static class NamedThreadFactory implements ThreadFactory {
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    NamedThreadFactory(String name) {
      SecurityManager s = System.getSecurityManager();
      group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
      this.namePrefix = name + "-";
    }

    public Thread newThread(Runnable r) {
      Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
      if (t.isDaemon()) t.setDaemon(false);
      if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
      return t;
    }
  }
}
