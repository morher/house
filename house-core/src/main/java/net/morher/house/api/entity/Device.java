package net.morher.house.api.entity;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import net.morher.house.api.entity.common.ConfigurableEntity;
import net.morher.house.api.entity.common.EntityOptions;

public class Device {
  private final EntityManager entityManager;
  @Getter private final DeviceId id;
  private DeviceInfo deviceInfo;

  private final Map<String, SubEntityEntry<?>> entities = new HashMap<>();

  public Device(EntityManager entityManager, DeviceId deviceId) {
    this.entityManager = entityManager;
    this.id = deviceId;
  }

  @SuppressWarnings("unchecked")
  public <E extends Entity> E entity(EntityDefinition<E> entityDefinition) {
    SubEntityEntry<?> entry = entities.get(entityDefinition.getEntityName());
    if (entry != null) {
      if (!entry.getEntityDefinition().equals(entityDefinition)) {
        throw new IllegalStateException(
            "Incompatible entity definition for sub-entity " + entityDefinition.getEntityName());
      }
      return (E) entry.getEntity();
    }
    E entity = entityDefinition.createEntity(entityManager, id);
    entities.put(entityDefinition.getEntityName(), new SubEntityEntry<>(entityDefinition, entity));
    if (deviceInfo != null) {
      entity.setDeviceInfo(deviceInfo);
    }
    return entity;
  }

  public void setDeviceInfo(DeviceInfo deviceInfo) {
    this.deviceInfo = deviceInfo;
  }

  public <O extends EntityOptions, E extends ConfigurableEntity<O>> E entity(
      EntityDefinition<E> entityDefinition, O options) {
    E entity = entity(entityDefinition);
    entity.setOptions(options);
    return entity;
  }

  @Data
  private static class SubEntityEntry<E extends Entity> {
    private final EntityDefinition<E> entityDefinition;
    private final E entity;
  }
}
