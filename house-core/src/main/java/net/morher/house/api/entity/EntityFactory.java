package net.morher.house.api.entity;

public interface EntityFactory<E extends Entity> {
  E createEntity(EntityManager entityManager, EntityId entityId);
}
