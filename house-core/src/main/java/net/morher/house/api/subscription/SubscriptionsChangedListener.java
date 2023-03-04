package net.morher.house.api.subscription;

public interface SubscriptionsChangedListener {

  void onSubscriptionsChanged();

  public static class SubscriptionsChangedListenerAdapter
      implements SubscriptionRegistryListener<Object> {

    private final SubscriptionsChangedListener delegate;

    public SubscriptionsChangedListenerAdapter(SubscriptionsChangedListener delegate) {
      this.delegate = delegate;
    }

    @Override
    public void onSubscribe(Object subscriber) {
      delegate.onSubscriptionsChanged();
    }

    @Override
    public void onUnsubscribe(Object subscriber) {
      delegate.onSubscriptionsChanged();
    }
  }
}
