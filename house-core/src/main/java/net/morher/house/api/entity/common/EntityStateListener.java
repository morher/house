package net.morher.house.api.entity.common;

public interface EntityStateListener<S> {
  void onStateUpdated(S state);
}
