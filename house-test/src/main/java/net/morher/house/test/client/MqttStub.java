package net.morher.house.test.client;

import net.morher.house.api.mqtt.client.MqttMessageListener;
import net.morher.house.api.subscription.Subscription;

public interface MqttStub {
    void publish(String topic, byte[] payload, boolean retain);

    Subscription subscribe(String topic, MqttMessageListener listener);
}