package net.morher.house.api.entity;

import lombok.Data;

@Data
public class EntityDefinition<E extends Entity> {
    private final String entityName;
    private final EntityFactory<E> factory;

    public E createEntity(EntityManager entityManager, DeviceId deviceId) {
        return factory.createEntity(entityManager, new EntityId(deviceId, entityName));
    }
}
