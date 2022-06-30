package net.morher.house.api.entity.light;

import java.util.Objects;

import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.common.CommandableEntity;
import net.morher.house.api.entity.common.EntityOptions;
import net.morher.house.api.entity.common.EntityStateListener;
import net.morher.house.api.subscription.Subscription;

public abstract class BaseStateHandler<S, O extends EntityOptions, C> {
    protected final CommandableEntity<S, O, C> entity;
    protected final EntityStateListener<? super S> delegate;
    protected final S defaultState;
    protected final Subscription stateSubscription;
    protected final Subscription commandSubscription;
    protected S currentState;

    public BaseStateHandler(CommandableEntity<S, O, C> entity, EntityStateListener<? super S> delegate, S defaultState) {
        this.entity = entity;
        this.delegate = delegate;
        this.defaultState = defaultState;
        this.commandSubscription = entity.command()
                .subscribe(this::handleCommandReceived);
        this.stateSubscription = entity.state()
                .subscribe(this::handleStateReceived);
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.entity.setDeviceInfo(deviceInfo);
    }

    public void updateOptions(O options) {
        entity.setOptions(options);
    }

    protected void handleCommandReceived(C command) {
        if (currentState == null) {
            this.currentState = defaultState;
        }
        currentState = modifyState(currentState, command);
        delegate.onStateUpdated(currentState);
        entity.publishState(currentState);
    }

    protected void handleStateReceived(S lampState) {
        S prevState = currentState;
        currentState = refreshState(currentState, lampState);

        // Only forward to delegate if state is changed to prevent an infinite reaction loop.
        // The state would normally be updated by this handler as a response to a command anyway.
        // The delegate would be notified then.
        if (!Objects.equals(currentState, prevState)) {
            delegate.onStateUpdated(lampState);
        }
    }

    public void updateState(S state) {
        S prevState = currentState;
        this.currentState = state;
        if (!currentState.equals(prevState)) {
            entity.publishState(state);
        }
    }

    public void disconnect() {
        commandSubscription.unsubscribe();
        stateSubscription.unsubscribe();
    }

    protected abstract S modifyState(S currentState, C command);

    protected S refreshState(S currentState, S reportedState) {
        return reportedState;
    }
}