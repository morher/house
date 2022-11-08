package net.morher.house.test.subscription;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.function.Consumer;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import net.morher.house.api.subscription.Subscribable;
import net.morher.house.api.subscription.Subscription;

@SuppressWarnings("resource")
public class TestSubscriberTest {

    private final Subscription sub = Mockito.mock(Subscription.class);
    @SuppressWarnings("unchecked")
    private final ArgumentCaptor<Consumer<String>> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);
    @SuppressWarnings("unchecked")
    private final Subscribable<String> subscribable = mock(Subscribable.class);

    @Test
    public void testSubscribe() {
        new TestSubscriber<>(subscribable);

        verify(subscribable).subscribe(any());
    }

    @Test
    public void testClose() {
        doReturn(sub).when(subscribable).subscribe(any());

        TestSubscriber<String> subscriber = new TestSubscriber<>(subscribable);
        verifyNoInteractions(sub);

        subscriber.close();
        verify(sub).unsubscribe();
    }

    @Test
    public void testReceiveItem() {
        doReturn(sub).when(subscribable).subscribe(consumerCaptor.capture());

        TestSubscriber<String> subscriber = new TestSubscriber<>(subscribable);

        assertThat(subscriber.items(), is(not(nullValue())));
        assertThat(subscriber.items().size(), is(0));
        assertThat(subscriber.size(), is(0));
        assertThat(subscriber.lastItem(), is(nullValue()));

        Consumer<String> consumer = consumerCaptor.getValue();
        consumer.accept("test value 1");

        assertThat(subscriber.items(), is(not(nullValue())));
        assertThat(subscriber.items().size(), is(1));
        assertThat(subscriber.size(), is(1));
        assertThat(subscriber.items(), hasItems("test value 1"));
        assertThat(subscriber.lastItem(), is("test value 1"));

        consumer.accept("test value 2");

        assertThat(subscriber.items(), is(not(nullValue())));
        assertThat(subscriber.items().size(), is(2));
        assertThat(subscriber.size(), is(2));
        assertThat(subscriber.items(), hasItems("test value 1", "test value 2"));
        assertThat(subscriber.lastItem(), is("test value 2"));
    }

    @Test
    public void testClear() {
        doReturn(sub).when(subscribable).subscribe(consumerCaptor.capture());

        TestSubscriber<String> subscriber = new TestSubscriber<>(subscribable);

        Consumer<String> consumer = consumerCaptor.getValue();
        consumer.accept("test value 1");
        consumer.accept("test value 2");
        subscriber.reset();

        consumer.accept("test value 3");

        assertThat(subscriber.items(), is(not(nullValue())));
        assertThat(subscriber.items().size(), is(1));
        assertThat(subscriber.size(), is(1));
        assertThat(subscriber.items(), hasItems("test value 3"));
        assertThat(subscriber.lastItem(), is("test value 3"));
    }
}
