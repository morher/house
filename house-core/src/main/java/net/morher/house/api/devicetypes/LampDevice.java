package net.morher.house.api.devicetypes;

import net.morher.house.api.entity.EntityDefinition;
import net.morher.house.api.entity.light.LightEntity;

/**
 * Common {@link EntityDfinition entity definitions} for lamps. The most common entity is probably
 * {@link #LIGHT light}.
 *
 * @author Morten Hermansen
 */
public abstract class LampDevice {

  /** The lamps primary light should be controlled with the {@link LightEntity} light. */
  public static final EntityDefinition<LightEntity> LIGHT =
      new EntityDefinition<>("Light", (em, id) -> em.lightEntity(id));
}
