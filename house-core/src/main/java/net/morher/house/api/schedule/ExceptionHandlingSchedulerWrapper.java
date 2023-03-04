package net.morher.house.api.schedule;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExceptionHandlingSchedulerWrapper implements ScheduledExecutorService {
  private final ScheduledExecutorService delegate;
  private final ScheduledTaskExceptionHandler exceptionHandler;

  public ExceptionHandlingSchedulerWrapper(
      ScheduledExecutorService delegate, ScheduledTaskExceptionHandler exceptionHandler) {

    this.delegate = delegate;
    this.exceptionHandler = exceptionHandler;
  }

  public interface ScheduledTaskExceptionHandler {
    void onException(Throwable e, Object task);
  }

  @Override
  public void shutdown() {
    delegate.shutdown();
  }

  @Override
  public List<Runnable> shutdownNow() {
    return shutdownNow();
  }

  @Override
  public boolean isShutdown() {
    return delegate.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return delegate.isTerminated();
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    return delegate.awaitTermination(timeout, unit);
  }

  @Override
  public <T> Future<T> submit(Callable<T> task) {
    return delegate.submit(new CallableWrapper<>(task));
  }

  @Override
  public <T> Future<T> submit(Runnable task, T result) {
    return delegate.submit(new RunnableWrapper(task), result);
  }

  @Override
  public Future<?> submit(Runnable task) {
    return delegate.submit(new RunnableWrapper(task));
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
      throws InterruptedException {
    return delegate.invokeAll(tasks);
  }

  @Override
  public <T> List<Future<T>> invokeAll(
      Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException {
    return delegate.invokeAll(tasks, timeout, unit);
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
      throws InterruptedException, ExecutionException {
    return delegate.invokeAny(tasks);
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    return delegate.invokeAny(tasks, timeout, unit);
  }

  @Override
  public void execute(Runnable command) {
    delegate.execute(new RunnableWrapper(command));
  }

  @Override
  public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
    return delegate.schedule(new RunnableWrapper(command), delay, unit);
  }

  @Override
  public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
    return delegate.schedule(new CallableWrapper<>(callable), delay, unit);
  }

  @Override
  public ScheduledFuture<?> scheduleAtFixedRate(
      Runnable command, long initialDelay, long period, TimeUnit unit) {
    return delegate.scheduleAtFixedRate(new RunnableWrapper(command), initialDelay, period, unit);
  }

  @Override
  public ScheduledFuture<?> scheduleWithFixedDelay(
      Runnable command, long initialDelay, long delay, TimeUnit unit) {
    return delegate.scheduleWithFixedDelay(new RunnableWrapper(command), initialDelay, delay, unit);
  }

  private class RunnableWrapper implements Runnable {
    private final Runnable delegate;

    public RunnableWrapper(Runnable delegate) {
      this.delegate = delegate;
    }

    @Override
    public void run() {
      try {
        delegate.run();
      } catch (Throwable e) {
        exceptionHandler.onException(e, delegate);
      }
    }
  }

  private class CallableWrapper<V> implements Callable<V> {
    private final Callable<V> delegate;

    public CallableWrapper(Callable<V> delegate) {
      this.delegate = delegate;
    }

    @Override
    public V call() throws Exception {
      try {
        return delegate.call();
      } catch (Throwable e) {
        exceptionHandler.onException(e, delegate);
      }
      return null;
    }
  }
}
