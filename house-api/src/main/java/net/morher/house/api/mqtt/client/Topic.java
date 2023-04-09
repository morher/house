package net.morher.house.api.mqtt.client;

import java.util.function.Consumer;
import net.morher.house.api.mqtt.client.MqttMessageListener.ParsedMqttMessageListener;
import net.morher.house.api.mqtt.payload.PayloadFormat;
import net.morher.house.api.subscription.Subscribable;
import net.morher.house.api.subscription.Subscription;

public interface Topic<T> extends Subscribable<T> {

  String getTopic();

  <S> Topic<S> subTopic(String postFix, PayloadFormat<S> serializer);

  <S> Topic<S> subTopic(String postFix, PayloadFormat<S> serializer, boolean retainByDefault);

  Subscription subscribe(ParsedMqttMessageListener<? super T> listener);

  void publish(T message);

  void publish(T message, boolean retain);

  default TopicValueHandler<T> observer() {
    return observer(null);
  }

  default TopicValueHandler<T> observer(T fallbackValue) {
    return valueHandler(null, fallbackValue);
  }

  default TopicValueHandler<T> valueHandler(Consumer<? super T> listener) {
    return valueHandler(listener, null);
  }

  default TopicValueHandler<T> valueHandler(Consumer<? super T> listener, T fallbackValue) {
    return new TopicValueHandler<>(this, listener, fallbackValue);
  }
}
