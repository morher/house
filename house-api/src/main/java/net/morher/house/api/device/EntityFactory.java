package net.morher.house.api.device;

import net.morher.house.api.entity.Entity;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityManager;

public interface EntityFactory<E extends Entity> {
    E createEntity(EntityManager entityManager, EntityId entityId);
}
