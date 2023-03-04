package net.morher.house.api.mqtt.client;

import net.morher.house.api.mqtt.client.MqttMessageListener.ParsedMqttMessageListener;
import net.morher.house.api.mqtt.payload.PayloadFormat;
import net.morher.house.api.state.StateObserver;
import net.morher.house.api.subscription.Subscribable;
import net.morher.house.api.subscription.Subscription;

public interface Topic<T> extends Subscribable<T> {

  String getTopic();

  <S> Topic<S> subTopic(String postFix, PayloadFormat<S> serializer);

  <S> Topic<S> subTopic(String postFix, PayloadFormat<S> serializer, boolean retainByDefault);

  Subscription subscribe(ParsedMqttMessageListener<? super T> listener);

  void publish(T message);

  void publish(T message, boolean retain);

  default StateObserver<T> observer() {
    return new StateObserver<>(this);
  }

  default StateObserver<T> observer(T fallbackValue) {
    return new StateObserver<>(this, fallbackValue);
  }
}
