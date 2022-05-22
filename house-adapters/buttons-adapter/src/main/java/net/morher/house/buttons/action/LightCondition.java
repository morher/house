package net.morher.house.buttons.action;

import net.morher.house.api.entity.common.EntityStateListener;
import net.morher.house.api.entity.common.StatefullEntity;
import net.morher.house.api.entity.light.LightState;

public class LightCondition implements Condition, EntityStateListener<LightState> {
    private final LightState conditionState;
    private LightState currentState = new LightState();

    public LightCondition(StatefullEntity<LightState, ?> lamp, LightState conditionState) {
        lamp.state().subscribe(this);
        this.conditionState = conditionState;
    }

    @Override
    public void onStateUpdated(LightState state) {
        this.currentState = state;
    }

    @Override
    public boolean isMatch() {
        return matches(conditionState.getState(), currentState.getState())
                && matches(conditionState.getBrightness(), currentState.getBrightness())
                && matches(conditionState.getEffect(), currentState.getEffect());
    }

    private <T> boolean matches(T conditionValue, T actual) {
        return conditionValue == null
                || conditionValue.equals(actual);
    }
}
