package net.morher.house.api.entity;

public interface EntityCommandListener<C> {
    void onCommand(C command);
}
