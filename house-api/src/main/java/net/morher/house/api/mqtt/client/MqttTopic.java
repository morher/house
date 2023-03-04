package net.morher.house.api.mqtt.client;

import java.util.function.Consumer;
import net.morher.house.api.mqtt.client.MqttMessageListener.ParsedMqttMessageListener;
import net.morher.house.api.mqtt.payload.PayloadFormat;
import net.morher.house.api.subscription.Subscription;

public class MqttTopic<T> implements Topic<T> {
  private final HouseMqttClient mqtt;
  private final String topic;
  private final PayloadFormat<T> serializer;
  private final boolean retainByDefault;

  public MqttTopic(HouseMqttClient mqtt, String topic, PayloadFormat<T> serializer) {
    this(mqtt, topic, serializer, false);
  }

  public MqttTopic(
      HouseMqttClient mqtt, String topic, PayloadFormat<T> serializer, boolean retainByDefault) {
    this.mqtt = mqtt;
    this.topic = topic;
    this.serializer = serializer;
    this.retainByDefault = retainByDefault;
  }

  @Override
  public String getTopic() {
    return topic;
  }

  @Override
  public <S> Topic<S> subTopic(String postFix, PayloadFormat<S> serializer) {
    return new MqttTopic<>(mqtt, topic + postFix, serializer);
  }

  @Override
  public <S> Topic<S> subTopic(
      String postFix, PayloadFormat<S> serializer, boolean retainByDefault) {
    return new MqttTopic<>(mqtt, topic + postFix, serializer, retainByDefault);
  }

  @Override
  public Subscription subscribe(ParsedMqttMessageListener<? super T> listener) {
    return mqtt.subscribe(topic, MqttMessageListener.map(serializer).thenNotify(listener));
  }

  @Override
  public Subscription subscribe(Consumer<? super T> listener) {
    return mqtt.subscribe(topic, MqttMessageListener.map(serializer).thenNotify(listener));
  }

  @Override
  public void publish(T message) {
    publish(message, retainByDefault);
  }

  @Override
  public void publish(T message, boolean retain) {
    mqtt.publish(topic, serializer.serialize(message), retain);
  }
}
