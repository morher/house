package net.morher.house.api.mqtt.client;

import java.util.function.Supplier;

import net.morher.house.api.subscription.Subscription;

public class StateObserver<T> implements Supplier<T>, Subscription {
    private Subscription subscription;
    private T currentValue;

    public StateObserver(MqttTopicManager<T> mqttTopic) {
        this(mqttTopic, null);
    }

    public StateObserver(MqttTopicManager<T> mqttTopic, T fallbackValue) {
        this.subscription = mqttTopic.subscribe(this::updateValue);
        this.currentValue = fallbackValue;
    }

    private void updateValue(T newValue) {
        this.currentValue = newValue;
    }

    @Override
    public T get() {
        return currentValue;
    }

    @Override
    public void unsubscribe() {
        subscription.unsubscribe();
    }
}
