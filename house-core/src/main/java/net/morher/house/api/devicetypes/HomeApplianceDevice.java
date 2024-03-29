package net.morher.house.api.devicetypes;

import net.morher.house.api.entity.EntityDefinition;
import net.morher.house.api.entity.EntityManager;
import net.morher.house.api.entity.sensor.SensorEntity;

public class HomeApplianceDevice {
  /**
   * A {@link SensorEntity} definition for the program phase.
   *
   * <p>Examples: Washing machine washing phase, Dishwasher washing phase
   */
  public static final EntityDefinition<SensorEntity<String>> PROGRAM_PHASE =
      new EntityDefinition<>("Program phase", EntityManager::stringSensorEntity);

  /**
   * A {@link SensorEntity} definition for the time the current program will complete.
   *
   * <p>Examples: 21:20
   */
  public static final EntityDefinition<SensorEntity<String>> ESTIMATED_COMPLETE_TIME =
      new EntityDefinition<>("Estimated complete time", EntityManager::stringSensorEntity);

  /** A {@link SensorEntity} definition for the remaining time for the current program. */
  public static final EntityDefinition<SensorEntity<Integer>> REMAINING_TIME =
      new EntityDefinition<>("Remaining time", EntityManager::integerSensorEntity);
}
