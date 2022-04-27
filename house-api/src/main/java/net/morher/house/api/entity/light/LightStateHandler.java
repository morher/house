package net.morher.house.api.entity.light;

import net.morher.house.api.device.DeviceInfo;
import net.morher.house.api.entity.EntityStateListener;
import net.morher.house.api.subscription.Subscription;

public class LightStateHandler {
    private final LightState defaultState = LightState.defaultState();
    private final LightEntity lightEntity;
    private final EntityStateListener<LightState> delegate;
    private final Subscription stateSubscription;
    private final Subscription commandSubscription;
    private LightState currentState;

    public LightStateHandler(LightEntity lightEntity, DeviceInfo deviceInfo, EntityStateListener<LightState> delegate) {
        this.lightEntity = lightEntity;
        this.delegate = delegate;
        this.commandSubscription = lightEntity.command()
                .subscribe(this::handleCommandReceived);
        this.stateSubscription = lightEntity.state()
                .subscribe(this::handleStateReceived);
        this.lightEntity.setDeviceInfo(deviceInfo);
    }

    public void updateOptions(LightOptions options) {
        lightEntity.setOptions(options);
    }

    private void handleCommandReceived(LightState command) {
        modifyState(command);
        delegate.onStateUpdated(currentState);
        lightEntity.publishState(currentState);
    }

    private void handleStateReceived(LightState lampState) {
        if (modifyState(lampState)) {
            delegate.onStateUpdated(lampState);
        }
    }

    public void updateLightState(LightState lightState) {
        LightState prevState = currentState;
        this.currentState = lightState;
        if (!currentState.equals(prevState)) {
            lightEntity.publishState(lightState);
        }
    }

    private boolean modifyState(LightState command) {
        LightState prevState = currentState;
        if (prevState == null) {
            prevState = defaultState;
        }
        currentState = prevState.modify(command);

        return !prevState.equals(currentState);
    }

    public void disconnect() {
        commandSubscription.unsubscribe();
        stateSubscription.unsubscribe();
    }

}
