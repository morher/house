package net.morher.house.test.subscription;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import net.morher.house.api.subscription.Subscribable;
import net.morher.house.api.subscription.Subscription;

/**
 * <p>
 * Test tool for recording items received through a subscription.
 * 
 * <p>
 * Items are internally stored in a list. The list can be retrieved through {@link #items()} or just the last item through
 * {@link #lastItem()}. Already received items can be cleared with {@link #reset()}.
 * 
 * <p>
 * In cases where a test need to send an item and that message should not be recorded by the TestSubscriber the item can be
 * skipped through {@link #addSkip(Matcher)} or {@link #addSkipEqual(Object)}.
 * 
 * @author Morten Hermansen
 */
public class TestSubscriber<T> implements Closeable {
    private final Subscription subscription;
    private final List<T> items = new ArrayList<>();
    private Queue<Matcher<? super T>> skipNextItems = new LinkedList<>();

    /**
     * The constructor takes a {@link Subscribable}. It will subscribe to it and store the received items in an internal list.
     * 
     * @param subscribable
     */
    public TestSubscriber(Subscribable<T> subscribable) {
        subscription = subscribable.subscribe(this::add);
    }

    private void add(T item) {
        Matcher<? super T> skipNextMatcher = skipNextItems.poll();
        if (skipNextMatcher != null) {
            MatcherAssert.assertThat("Skipped item must be as specified", item, skipNextMatcher);
        } else {
            items.add(item);
        }
    }

    /**
     * Retrieve a snapshot of the currently received items list.
     * 
     * @return A list of items received through the subscription until now.
     */
    public List<T> items() {
        return new ArrayList<>(items);
    }

    /**
     * Returns the number of items currently stored.
     * 
     * @return The number of items.
     */
    public int size() {
        return items.size();
    }

    /**
     * Returns the last item received, or {@code null} if no items were received yet.
     * 
     * @return The last item or {@code null}.
     */
    public T lastItem() {
        if (items.isEmpty()) {
            return null;
        }
        return items.get(items.size() - 1);
    }

    /**
     * Adds to the skip queue. Skipped items are not stored and will not be reported back by {@link #items}, {@link #lastItem()}
     * or {@link #size()}. The skipped items are checked against the given {@link Matcher} to make sure the skipped message is
     * the expected one.
     * 
     * @param nextItemsMatcher
     */
    public void addSkip(Matcher<? super T> nextItemsMatcher) {
        skipNextItems.add(nextItemsMatcher);
    }

    /**
     * Convenience method for {@link #addSkip(Matcher)}, where the default {@link Matcher} {@code is(equalTo(item))} is applied.
     * 
     * @param item
     */
    public void addSkipEqual(T item) {
        skipNextItems.add(is(equalTo(item)));
    }

    /**
     * Removes all stored items.
     */
    public void reset() {
        items.clear();
    }

    /**
     * Unsubscribes the subscription.
     */
    public void close() {
        subscription.unsubscribe();
    }
}
