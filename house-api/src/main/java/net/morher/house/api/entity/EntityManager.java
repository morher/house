package net.morher.house.api.entity;

import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.entity.sensor.SensorEntity;
import net.morher.house.api.entity.switches.SwitchEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.payload.NumberMessage;
import net.morher.house.api.mqtt.payload.RawMessage;
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

    public SensorEntity<Double> decimalSensorEntity(EntityId entityId) {
        return new SensorEntity<>(client, entityId, entityChanges.getDispatcher(), NumberMessage.decimal());
    }

    public SensorEntity<Integer> integerSensorEntity(EntityId entityId) {
        return new SensorEntity<>(client, entityId, entityChanges.getDispatcher(), NumberMessage.integer());
    }

    public LightEntity lightEntity(EntityId entityId) {
        return new LightEntity(client, entityId, entityChanges.getDispatcher());
    }

    public SensorEntity<String> stringSensorEntity(EntityId entityId) {
        return new SensorEntity<>(client, entityId, entityChanges.getDispatcher(), RawMessage.toStr());
    }

    public SwitchEntity switchEntity(EntityId entityId) {
        return new SwitchEntity(client, entityId, entityChanges.getDispatcher());
    }

    public SubscriptionTopic<EntityListener> entityChanges() {
        return entityChanges;
    }
}
