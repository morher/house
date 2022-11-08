package net.morher.house.test.subscription;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import net.morher.house.api.subscription.Subscribable;
import net.morher.house.api.subscription.Subscription;

public class TestSubscriber<T> implements Closeable {
    private final Subscription subscription;
    private final List<T> items = new ArrayList<>();

    public TestSubscriber(Subscribable<T> subscribable) {
        subscription = subscribable.subscribe(items::add);
    }

    public List<T> items() {
        return items;
    }

    public int size() {
        return items.size();
    }

    public T lastItem() {
        if (items.isEmpty()) {
            return null;
        }
        return items.get(items.size() - 1);
    }

    public void reset() {
        items.clear();
    }

    public void close() {
        subscription.unsubscribe();
    }
}
