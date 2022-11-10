package net.morher.house.api.entity.common;

public interface EntityCommandListener<C> {
    void onCommand(C command);
}
