package net.morher.house.api.subscription;

import java.util.function.Consumer;

public interface Subscribable<T> {

    Subscription subscribe(Consumer<? super T> listener);

}