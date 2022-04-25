package net.morher.house.api.device;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import net.morher.house.api.entity.Entity;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityManager;

public class Device<M extends Entity> {
    private final EntityManager entityManager;
    private final DeviceId deviceId;
    private final M mainEntity;

    private final Map<String, SubEntityEntry<?>> entities = new HashMap<>();

    public Device(EntityManager entityManager, DeviceId deviceId) {
        this.entityManager = entityManager;
        this.deviceId = deviceId;
        this.mainEntity = null;
    }

    public Device(
            EntityManager entityManager,
            DeviceId deviceId,
            EntityFactory<? extends M> entityFactory) {

        this.entityManager = entityManager;
        this.deviceId = deviceId;
        this.mainEntity = entityFactory
                .createEntity(entityManager, new EntityId(deviceId, null));
    }

    public M getMainEntity() {
        return mainEntity;
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> E getEntity(EntityDefinition<E> entityDefinition) {
        SubEntityEntry<?> entry = entities.get(entityDefinition.getEntityName());
        if (entry != null) {
            if (!entry.getEntityDefinition().equals(entityDefinition)) {
                throw new IllegalStateException("Incompatible entity definition for sub-entity " + entityDefinition.getEntityName());
            }
            return (E) entry.getEntity();

        }
        E entity = entityDefinition.createEntity(entityManager, deviceId);
        entities.put(entityDefinition.getEntityName(), new SubEntityEntry<>(entityDefinition, entity));
        return entity;
    }

    @Data
    private static class SubEntityEntry<E extends Entity> {
        private final EntityDefinition<E> entityDefinition;
        private final E entity;
    }
}
