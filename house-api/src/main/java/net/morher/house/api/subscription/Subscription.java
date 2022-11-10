package net.morher.house.api.subscription;

public interface Subscription extends AutoCloseable {
    void unsubscribe();

    default void close() {
        unsubscribe();
    }
}
