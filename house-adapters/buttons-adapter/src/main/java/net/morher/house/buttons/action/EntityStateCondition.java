package net.morher.house.buttons.action;

import java.util.Objects;

import net.morher.house.api.entity.common.EntityStateListener;
import net.morher.house.api.entity.common.StatefullEntity;

public class EntityStateCondition<T> implements Condition, EntityStateListener<T> {
    private final T conditionState;
    private T currentState;

    public EntityStateCondition(StatefullEntity<T, ?> lamp, T conditionState) {
        lamp.state().subscribe(this);
        this.conditionState = conditionState;
    }

    @Override
    public void onStateUpdated(T state) {
        this.currentState = state;
    }

    @Override
    public boolean isMatch() {
        return Objects.equals(currentState, conditionState);
    }
}
