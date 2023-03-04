package net.morher.house.api.schedule;

public class NamedTask implements Runnable, ScheduledRunnable {
  private final Object delegate;
  private final String name;

  public NamedTask(ScheduledRunnable delegate, String name) {
    this.delegate = delegate;
    this.name = name;
  }

  @Override
  public void run() {
    if (delegate instanceof ScheduledRunnable) {
      try {
        ((ScheduledRunnable) delegate).runScheduled();

      } catch (Reschedule r) {
        throw new RuntimeException("Not run in reschedulable context", r);
      }
    }
    if (delegate instanceof Runnable) {
      ((Runnable) delegate).run();
    }
  }

  @Override
  public void runScheduled() throws Reschedule {
    if (delegate instanceof ScheduledRunnable) {
      ((ScheduledRunnable) delegate).runScheduled();
    }
    if (delegate instanceof Runnable) {
      ((Runnable) delegate).run();
    }
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj.equals(delegate);
  }
}
