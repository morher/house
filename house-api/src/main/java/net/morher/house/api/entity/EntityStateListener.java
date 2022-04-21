package net.morher.house.api.entity;

public interface EntityStateListener<S> {
    void onStateUpdated(S state);
}
