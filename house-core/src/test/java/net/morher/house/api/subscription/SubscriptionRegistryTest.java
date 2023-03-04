package net.morher.house.api.subscription;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SubscriptionRegistryTest {

  @Test
  public void testDispatcher() {
    SubscriptionRegistry<TestListener> registry = new SubscriptionRegistry<>(TestListener.class);

    TestListener subscriber1 = mock(TestListener.class);
    registry.subscribe(subscriber1);
    TestListener subscriber2 = mock(TestListener.class);
    registry.subscribe(subscriber2);

    registry.getDispatcher().aMethod();

    verify(subscriber1, times(1)).aMethod();
    verify(subscriber2, times(1)).aMethod();
  }

  @Test
  public void testDispatcherDefaultImplementation() {
    SubscriptionRegistry<TestListener> registry = new SubscriptionRegistry<>(TestListener.class);

    TestListener subscriber1 = mock(TestListener.class);
    registry.subscribe(subscriber1);
    TestListener subscriber2 = mock(TestListener.class);
    registry.subscribe(subscriber2);

    registry.getDispatcher().methodWithDefaultImplementation();

    verify(subscriber1, times(1)).methodWithDefaultImplementation();
    verify(subscriber2, times(1)).methodWithDefaultImplementation();
  }

  @Test
  public void testDispatcherWithReturnValue() {
    SubscriptionRegistry<TestListener> registry = new SubscriptionRegistry<>(TestListener.class);

    TestListener subscriber1 = mock(TestListener.class);
    registry.subscribe(subscriber1);
    TestListener subscriber2 = mock(TestListener.class);
    registry.subscribe(subscriber2);

    registry.getDispatcher().methodWithReturnValue();

    verify(subscriber1, times(1)).methodWithReturnValue();
    verify(subscriber2, times(1)).methodWithReturnValue();
  }

  @Test
  public void testDispatcherWithParameter() {
    SubscriptionRegistry<TestListener> registry = new SubscriptionRegistry<>(TestListener.class);

    TestListener subscriber1 = mock(TestListener.class);
    registry.subscribe(subscriber1);
    TestListener subscriber2 = mock(TestListener.class);
    registry.subscribe(subscriber2);

    registry.getDispatcher().methodWithParameter("Test");

    verify(subscriber1, times(1)).methodWithParameter(eq("Test"));
    verify(subscriber2, times(1)).methodWithParameter(eq("Test"));
  }

  @Test
  public void testToString() {
    SubscriptionRegistry<TestListener> registry = new SubscriptionRegistry<>(TestListener.class);

    assertThat(
        registry.getDispatcher().toString(), is(equalTo("SubscriptionRegistry for TestListener")));
  }

  private interface TestListener {
    void aMethod();

    default void methodWithDefaultImplementation() {}

    String methodWithReturnValue();

    void methodWithParameter(String parameter);
  }
}
