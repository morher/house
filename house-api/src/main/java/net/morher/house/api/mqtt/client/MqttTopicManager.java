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
    private final boolean retainByDefault;
    private Subscription subscription;

    public MqttTopicManager(HouseMqttClient mqtt, String topic, PayloadFormat<T> serializer) {
        this(mqtt, topic, serializer, false, null);
    }

    public MqttTopicManager(HouseMqttClient mqtt, String topic, PayloadFormat<T> serializer, boolean retainByDefault) {
        this(mqtt, topic, serializer, retainByDefault, null);
    }

    public MqttTopicManager(HouseMqttClient mqtt, String topic, PayloadFormat<T> serializer, boolean retainByDefault, ParsedMqttMessageListener<? super T> listener) {
        this.mqtt = mqtt;
        this.topic = topic;
        this.serializer = serializer;
        this.listener = listener != null
                ? MqttMessageListener.map(serializer).thenNotify(listener)
                : null;
        this.retainByDefault = retainByDefault;
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

    public <S> MqttTopicManager<S> subTopic(String postFix, PayloadFormat<S> serializer, boolean retainByDefault) {
        return new MqttTopicManager<>(mqtt, topic + postFix, serializer, retainByDefault);
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

    public void publish(T message) {
        publish(message, retainByDefault);
    }

    public void publish(T message, boolean retain) {
        mqtt.publish(topic, serializer.serialize(message), retain);
    }

    public StateObserver<T> observer() {
        return new StateObserver<>(this);
    }

    public StateObserver<T> observer(T fallbackValue) {
        return new StateObserver<>(this, fallbackValue);
    }
}
