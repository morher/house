package net.morher.house.test.client;

import lombok.Getter;
import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttMessageListener;
import net.morher.house.api.subscription.Subscription;

@Getter
public class TestHouseMqttClient implements HouseMqttClient {
    private final MqttNamespace namespace = MqttNamespace.defaultNamespace();
    private final MqttStub mqttStub;

    public TestHouseMqttClient(MqttStub mqttStub) {
        this.mqttStub = mqttStub;
    }

    public static TestHouseMqttClient loopback() {
        return new TestHouseMqttClient(new DefaultMqttStub().loopback(true));
    }

    @Override
    public String getAvailabilityTopic() {
        return namespace.clientAvailabilityTopic("test-adapter");
    }

    @Override
    public void publish(String topic, byte[] payload, boolean retain) {
        mqttStub.publish(topic, payload, retain);
    }

    @Override
    public Subscription subscribe(String topic, MqttMessageListener listener) {
        return mqttStub.subscribe(topic, listener);
    }
}
