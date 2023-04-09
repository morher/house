package net.morher.house.api.mqtt.client;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.morher.house.api.subscription.Subscription;

public class TopicValueHandler<T> implements Supplier<T>, Subscription {
  private final Topic<T> topic;
  private final Consumer<? super T> listener;
  private final T fallbackValue;
  private final Subscription sub;
  private T currentValue;

  public TopicValueHandler(Topic<T> topic, Consumer<? super T> listener, T fallbackValue) {
    this.topic = topic;
    this.listener = listener;
    this.fallbackValue = fallbackValue;
    this.sub = topic.subscribe(this::onMessage);
  }

  private void onMessage(T newValue) {
    T previousValue = get();
    this.currentValue = newValue;
    if (listener != null && !Objects.equals(previousValue, newValue)) {
      listener.accept(newValue);
    }
  }

  public boolean isInitialized() {
    return currentValue != null;
  }

  @Override
  public T get() {
    return currentValue != null ? currentValue : fallbackValue;
  }

  public void publish(T value) {
    this.currentValue = value;
    topic.publish(value);
  }

  @Override
  public void unsubscribe() {
    sub.unsubscribe();
  }
}
