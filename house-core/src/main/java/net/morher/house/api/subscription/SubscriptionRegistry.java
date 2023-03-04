package net.morher.house.api.subscription;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import net.morher.house.api.subscription.SubscriptionsChangedListener.SubscriptionsChangedListenerAdapter;

public class SubscriptionRegistry<L> implements SubscriptionTopic<L> {
  private final List<SubscriptionRegistryListener<? super L>> registryListeners = new ArrayList<>();
  private final Class<L> type;
  protected final List<L> subscribers = new ArrayList<>();
  private final L dispatcher;

  @SuppressWarnings("unchecked")
  public SubscriptionRegistry(Class<L> type) {
    this.type = type;
    this.dispatcher =
        (L)
            Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class[] {type}, this::handleMethodCall);
  }

  public void addSubscriptionListener(SubscriptionRegistryListener<? super L> listener) {
    registryListeners.add(listener);
  }

  public void addRegistryChangedListener(SubscriptionsChangedListener listener) {
    registryListeners.add(new SubscriptionsChangedListenerAdapter(listener));
  }

  @Override
  public Subscription subscribe(L subscriber) {
    if (!subscribers.contains(subscriber)) {
      subscribers.add(subscriber);
      registryListeners.forEach(l -> l.onSubscribe(subscriber));
      return new SubscriptionImpl(subscriber);
    }
    throw new IllegalStateException("Already subscribed: " + subscriber);
  }

  private void unsubscribe(L item) {
    if (subscribers.contains(item)) {
      subscribers.remove(item);
      registryListeners.forEach(l -> l.onUnsubscribe(item));
    }
  }

  public boolean isEmpty() {
    return subscribers.isEmpty();
  }

  public L getDispatcher() {
    return dispatcher;
  }

  @Override
  public String toString() {
    return "SubscriptionRegistry for " + type.getSimpleName();
  }

  private Object handleMethodCall(Object proxy, Method method, Object[] args) throws Throwable {
    Class<?> declaringClass = method.getDeclaringClass();
    Object ret = null;
    if (!declaringClass.isAssignableFrom(Object.class) && declaringClass.isAssignableFrom(type)) {
      for (L item : subscribers) {
        ret = method.invoke(item, args);
      }

    } else {
      ret = method.invoke(this, args);
    }

    return ret;
  }

  private class SubscriptionImpl implements Subscription {
    private final L subscriber;

    public SubscriptionImpl(L subscriber) {
      this.subscriber = subscriber;
    }

    @Override
    public void unsubscribe() {
      SubscriptionRegistry.this.unsubscribe(subscriber);
    }
  }
}
