package net.morher.house.api.mqtt.client;

import java.util.function.Consumer;

import net.morher.house.api.mqtt.client.MqttMessageListener.ParsedMqttMessageListener;
import net.morher.house.api.mqtt.payload.PayloadFormat;
import net.morher.house.api.subscription.Subscription;

public class MqttTopicManager<T> {
    private final HouseMqttClient mqtt;
    private final String topic;
    private final MqttMessageListener listener;
    private final PayloadFormat<T> serializer;
    private Subscription subscription;

    public MqttTopicManager(HouseMqttClient mqtt, String topic, PayloadFormat<T> serializer) {
        this(mqtt, topic, serializer, null);
    }

    public MqttTopicManager(HouseMqttClient mqtt, String topic, PayloadFormat<T> serializer, ParsedMqttMessageListener<? super T> listener) {
        this.mqtt = mqtt;
        this.topic = topic;
        this.serializer = serializer;
        this.listener = listener != null
                ? MqttMessageListener.map(serializer).thenNotify(listener)
                : null;

    }

    public String getTopic() {
        return topic;
    }

    public boolean isActive() {
        return subscription != null;
    }

    public <S> MqttTopicManager<S> subTopic(String postFix, PayloadFormat<S> serializer) {
        return new MqttTopicManager<>(mqtt, topic + postFix, serializer);
    }

    public Subscription subscribe(ParsedMqttMessageListener<? super T> listener) {
        return mqtt.subscribe(topic, MqttMessageListener.map(serializer).thenNotify(listener));
    }

    public Subscription subscribe(Consumer<? super T> listener) {
        return mqtt.subscribe(topic, MqttMessageListener.map(serializer).thenNotify(listener));
    }

    public MqttTopicManager<T> activateSubscription() {
        activateSubscription(true);
        return this;
    }

    public void activateSubscription(boolean active) {
        if (active && listener != null) {
            if (subscription == null) {
                subscription = mqtt.subscribe(topic, listener);
            }
        } else {
            if (subscription != null) {
                subscription.unsubscribe();
                subscription = null;
            }
        }
    }

    public void publish(T message, boolean retain) {
        mqtt.publish(topic, serializer.serialize(message), retain);
    }
}
