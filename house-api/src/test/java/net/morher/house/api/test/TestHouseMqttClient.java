package net.morher.house.api.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttTopic;

import lombok.Data;
import lombok.Getter;
import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.Message;
import net.morher.house.api.mqtt.client.MqttMessageListener;
import net.morher.house.api.mqtt.payload.PayloadFormat;
import net.morher.house.api.subscription.Subscription;

@Getter
public class TestHouseMqttClient implements HouseMqttClient {
    private final List<TestSubscription> subscriptions = new ArrayList<>();
    private final List<TestMessage> messages = new ArrayList<>();

    @Override
    public MqttNamespace getNamespace() {
        return MqttNamespace.defaultNamespace();
    }

    @Override
    public String getAvailabilityTopic() {
        return getNamespace().clientAvailabilityTopic("adapter");
    }

    @Override
    public void publish(String topic, byte[] payload, boolean retain) {
        messages.add(new TestMessage(topic, payload, 0, retain));
    }

    @Override
    public Subscription subscribe(String topic, MqttMessageListener listener) {
        TestSubscription subscription = new TestSubscription(topic, listener);
        subscriptions.add(subscription);
        return subscription;
    }

    public void simulateReceivedMessage(String topic, byte[] payload, int qos, boolean retain) {
        for (TestSubscription subscription : subscriptions) {
            if (subscription.isMatch(topic)) {
                subscription.getListener().onMessage(topic, payload, qos, retain);
            }
        }
    }

    public <T> void simulateReceivedMessage(String topic, T payload, PayloadFormat<? super T> format) {
        simulateReceivedMessage(topic, format.serialize(payload), 0, false);
    }

    public void resetCounters() {
        this.messages.clear();
    }

    @Data
    public class TestSubscription implements Subscription {
        private final String topicFilter;
        private final MqttMessageListener listener;

        public boolean isMatch(String topic) {
            return MqttTopic.isMatched(topicFilter, topic);
        }

        @Override
        public void unsubscribe() {
            subscriptions.remove(this);
        }
    }

    public class TestMessage extends Message {

        public TestMessage(String topic, byte[] payload, int qos, boolean retain) {
            super(topic, payload, qos, retain);
        }

        public <T> T getPayload(PayloadFormat<T> format) {
            return format.deserialize(getPayload());
        }
    }
}