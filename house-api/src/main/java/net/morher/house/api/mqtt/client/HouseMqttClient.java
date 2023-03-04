package net.morher.house.api.mqtt.client;

import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.api.mqtt.payload.PayloadFormat;
import net.morher.house.api.subscription.Subscription;

public interface HouseMqttClient {

  MqttNamespace getNamespace();

  String getAvailabilityTopic();

  void publish(String topic, byte[] payload, boolean retain);

  Subscription subscribe(String topic, MqttMessageListener listener);

  default <T> Topic<T> topic(String topicName, PayloadFormat<T> format) {
    return new MqttTopic<>(this, topicName, format);
  }

  default <T> Topic<T> topic(String topicName, PayloadFormat<T> format, boolean retainByDefault) {
    return new MqttTopic<>(this, topicName, format, retainByDefault);
  }
}
