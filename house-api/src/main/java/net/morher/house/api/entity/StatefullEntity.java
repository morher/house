package net.morher.house.api.entity;

import java.util.Objects;

import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttTopicManager;
import net.morher.house.api.mqtt.payload.PayloadFormat;
import net.morher.house.api.subscription.SubscriptionRegistry;
import net.morher.house.api.subscription.SubscriptionRegistryListener;
import net.morher.house.api.subscription.SubscriptionTopic;

public class StatefullEntity<S, O extends EntityOptions> extends ConfigurableEntity<O> {
    private final SubscriptionRegistry<EntityStateListener<S>> stateSubscriptions;
    protected final MqttTopicManager<S> stateTopic;
    protected S currentState;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public StatefullEntity(HouseMqttClient client, EntityId entityId, EntityListener entityListener, PayloadFormat<S> stateSerializer) {
        super(entityId, entityListener);

        stateSubscriptions = new SubscriptionRegistry(EntityStateListener.class);
        stateSubscriptions.addSubscriptionListener(new StateSubscriptionListener());

        this.stateTopic = new MqttTopicManager<>(
                client,
                client.getNamespace().entityStateTopic(entityId),
                stateSerializer,
                this::onStateChange);
    }

    public String getStateTopic() {
        return this.stateTopic.getTopic();
    }

    protected void onStateChange(String topic, S state, int qos, boolean retained) {
        if (Objects.equals(state, currentState)) {
            return;
        }
        currentState = state;
        stateSubscriptions.getDispatcher().onStateUpdated(currentState);
    }

    public S getCurrentState() {
        return currentState;
    }

    public void publishState(S state) {
        currentState = state;
        stateTopic.publish(state, true);
    }

    public SubscriptionTopic<EntityStateListener<S>> state() {
        return stateSubscriptions;
    }

    protected final void syncStateSubscriptions() {
        stateTopic.activateSubscription(!stateSubscriptions.isEmpty() || needStateSubscription());
    }

    protected boolean needStateSubscription() {
        return false;
    }

    private class StateSubscriptionListener implements SubscriptionRegistryListener<EntityStateListener<S>> {

        @Override
        public void onSubscribe(EntityStateListener<S> subscriber) {
            if (currentState != null) {
                subscriber.onStateUpdated(currentState);
            }
            syncStateSubscriptions();
        }

        @Override
        public void onUnsubscribe(EntityStateListener<S> subscriber) {
            syncStateSubscriptions();
        }
    }
}