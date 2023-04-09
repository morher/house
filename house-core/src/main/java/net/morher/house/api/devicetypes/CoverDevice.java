package net.morher.house.api.devicetypes;

import net.morher.house.api.entity.EntityDefinition;
import net.morher.house.api.entity.EntityManager;
import net.morher.house.api.entity.cover.CoverEntity;

public class CoverDevice {

  /**
   * A {@link CoverEntity} definition for covers.
   *
   * <p>Examples: Blinds, awnings, projector screens.
   */
  public static final EntityDefinition<CoverEntity> COVER =
      new EntityDefinition<>("Cover", EntityManager::coverEntity);
}
