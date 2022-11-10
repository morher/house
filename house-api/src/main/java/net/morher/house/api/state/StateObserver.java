package net.morher.house.api.state;

import java.util.function.Supplier;

import net.morher.house.api.subscription.Subscribable;
import net.morher.house.api.subscription.Subscription;

public class StateObserver<T> implements Supplier<T>, Subscription {
    private Subscription subscription;
    private T currentValue;

    public StateObserver(Subscribable<T> topic) {
        this(topic, null);
    }

    public StateObserver(Subscribable<T> topic, T fallbackValue) {
        this.subscription = topic.subscribe(this::updateValue);
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
