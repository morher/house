package net.morher.house.api.entity;

import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.entity.switches.SwitchEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.subscription.SubscriptionRegistry;
import net.morher.house.api.subscription.SubscriptionTopic;

public class EntityManager {
    private final HouseMqttClient client;
    private final SubscriptionRegistry<EntityListener> entityChanges = new SubscriptionRegistry<>(EntityListener.class);

    public EntityManager(HouseMqttClient client) {
        this.client = client;
    }

    public <E extends Entity> E entity(DeviceId device, EntityDefinition<E> entityDefinition) {
        return entityDefinition.createEntity(this, device);
    }

    public LightEntity lightEntity(EntityId entityId) {
        return new LightEntity(client, entityId, entityChanges.getDispatcher());
    }

    public SwitchEntity switchEntity(EntityId entityId) {
        return new SwitchEntity(client, entityId, entityChanges.getDispatcher());
    }

    public SubscriptionTopic<EntityListener> entityChanges() {
        return entityChanges;
    }
}
