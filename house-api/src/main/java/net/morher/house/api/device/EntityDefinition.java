package net.morher.house.api.device;

import lombok.Data;
import net.morher.house.api.entity.Entity;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityManager;

@Data
public class EntityDefinition<E extends Entity> {
    private final String entityName;
    private final EntityFactory<E> factory;

    public E createEntity(EntityManager entityManager, DeviceId deviceId) {
        return factory.createEntity(entityManager, new EntityId(deviceId, entityName));
    }
}
