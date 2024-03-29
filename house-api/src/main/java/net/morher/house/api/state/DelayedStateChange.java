package net.morher.house.api.state;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import net.morher.house.api.schedule.DelayedTrigger;
import net.morher.house.api.schedule.HouseScheduler;

public class DelayedStateChange<T> {
  private final HouseScheduler scheduler;
  private final DelayedTrigger trigger;
  private final Consumer<? super T> listener;
  private final Duration defaultDelay;
  private final Map<T, Duration> delayMapping = new HashMap<>();
  private T currentState;
  private StateEvent<T> lastReport;

  public DelayedStateChange(
      HouseScheduler scheduler, Consumer<? super T> listener, Duration defaultDelay) {
    this.scheduler = scheduler;
    this.trigger = scheduler.delayedTrigger("Check delayed state change", this::handleStateChange);
    this.listener = listener;
    this.defaultDelay = defaultDelay;
  }

  public DelayedStateChange<T> delayChangeTo(T newState, Duration delay) {
    delayMapping.put(newState, delay);
    return this;
  }

  public void reportState(T newState) {
    lastReport = new StateEvent<T>(newState, scheduler.now());
    Duration delay = delayMapping.getOrDefault(newState, defaultDelay);
    trigger.runAfter(delay);
  }

  private void handleStateChange() {
    StateEvent<T> report = lastReport;
    if (report == null || Objects.equals(currentState, report.getState())) {
      return;
    }

    Duration delay = delayMapping.getOrDefault(report.getState(), defaultDelay);

    Instant reportHandleTime = report.getEventTime().plus(delay);
    if (scheduler.now().isBefore(reportHandleTime)) {
      trigger.runAt(reportHandleTime);
      return;
    }
    currentState = report.getState();
    listener.accept(currentState);

    if (report != lastReport) {
      trigger.runNow();
    }
  }
}
