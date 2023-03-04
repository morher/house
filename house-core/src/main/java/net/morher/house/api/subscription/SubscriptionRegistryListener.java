package net.morher.house.api.subscription;

public interface SubscriptionRegistryListener<L> {
  void onSubscribe(L subscriber);

  void onUnsubscribe(L subscriber);
}
