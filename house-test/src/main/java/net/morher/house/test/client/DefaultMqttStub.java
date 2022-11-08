package net.morher.house.test.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttTopic;

import lombok.Data;
import net.morher.house.api.mqtt.client.Message;
import net.morher.house.api.mqtt.client.MqttMessageListener;
import net.morher.house.api.mqtt.payload.PayloadFormat;
import net.morher.house.api.subscription.Subscription;

public class DefaultMqttStub implements MqttStub {
    private final List<TestSubscription> subscriptions = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();
    private final Map<String, Message> retainedMessages = new HashMap<>();
    private boolean loopback;
    private boolean recordPublishedMessages;

    public DefaultMqttStub loopback(boolean loopback) {
        this.loopback = loopback;
        return this;
    }

    public DefaultMqttStub recordPublishedMessages(boolean recordPublishedMessages) {
        this.recordPublishedMessages = recordPublishedMessages;
        return this;
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

    @Override
    public void publish(String topic, byte[] payload, boolean retain) {
        if (recordPublishedMessages) {
            messages.add(new Message(topic, payload, 0, retain));
        }
        if (loopback) {
            simulateReceivedMessage(topic, payload, 0, retain);
            if (retain) {
                retainedMessages.put(topic, new Message(topic, payload, 0, retain));
            } else {
                retainedMessages.remove(topic);
            }
        }
    }

    @Override
    public Subscription subscribe(String topic, MqttMessageListener listener) {
        TestSubscription subscription = new TestSubscription(topic, listener);
        subscriptions.add(subscription);
        if (loopback) {
            for (Message message : retainedMessages.values()) {
                if (subscription.isMatch(message.getTopic())) {
                    subscription.getListener()
                            .onMessage(message.getTopic(), message.getPayload(), message.getQos(), message.isRetain());
                }
            }
        }
        return subscription;
    }

    public boolean isSubscribedTo(String topicFilter) {
        return subscriptions.stream()
                .filter(s -> s.getTopicFilter().equals(topicFilter))
                .findAny()
                .isPresent();
    }

    @Data
    private class TestSubscription implements Subscription {

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
}