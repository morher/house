package net.morher.house.api.entity.trigger;

import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.entity.common.ConfigurableEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttTopicManager;
import net.morher.house.api.mqtt.payload.RawMessage;
import net.morher.house.api.subscription.Subscription;
import net.morher.house.api.subscription.SubscriptionRegistry;

public class TriggerEntity extends ConfigurableEntity<TriggerOptions> {
    private final SubscriptionRegistry<EntityTriggerListener> triggerSubscriptions;
    protected final MqttTopicManager<String> triggerTopic;

    public TriggerEntity(HouseMqttClient client, EntityId id, EntityListener entityListener) {
        super(id, entityListener);

        triggerSubscriptions = new SubscriptionRegistry<>(EntityTriggerListener.class);
        triggerSubscriptions.addRegistryChangedListener(this::syncTriggerSubscriptions);

        triggerTopic = client.topic(
                client.getNamespace().entityTriggerTopic(id),
                RawMessage.toStr());
    }

    protected final void syncTriggerSubscriptions() {
        triggerTopic.activateSubscription(!triggerSubscriptions.isEmpty());
    }

    public Subscription subscribeOnTrigger(EntityTriggerListener listener) {
        return triggerTopic.subscribe(event -> listener.onTrigger(event));
    }

    public void publishEvent(String event) {
        triggerTopic.publish(event, false);
    }
}
