package net.morher.house.api.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

public class Device {
    private final EntityManager entityManager;
    private final DeviceId deviceId;

    private final Map<String, SubEntityEntry<?>> entities = new HashMap<>();

    public Device(EntityManager entityManager, DeviceId deviceId) {
        this.entityManager = entityManager;
        this.deviceId = deviceId;
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> E entity(EntityDefinition<E> entityDefinition) {
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
