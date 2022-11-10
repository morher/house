package net.morher.house.api.entity.trigger;

import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.entity.common.ConfigurableEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.Topic;
import net.morher.house.api.mqtt.payload.RawMessage;
import net.morher.house.api.subscription.Subscription;

public class TriggerEntity extends ConfigurableEntity<TriggerOptions> {
    protected final Topic<String> triggerTopic;

    public TriggerEntity(HouseMqttClient client, EntityId id, EntityListener entityListener) {
        super(id, entityListener);

        triggerTopic = client.topic(
                client.getNamespace().entityTriggerTopic(id),
                RawMessage.toStr(),
                false);
    }

    public Subscription subscribeOnTrigger(EntityTriggerListener listener) {
        return triggerTopic.subscribe(event -> listener.onTrigger(event));
    }

    public String getTriggerTopic() {
        return triggerTopic.getTopic();
    }

    public void publishEvent(String event) {
        triggerTopic.publish(event);
    }
}
