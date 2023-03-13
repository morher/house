package net.morher.house.api.devicetypes;

import net.morher.house.api.entity.EntityDefinition;
import net.morher.house.api.entity.sensor.SensorEntity;

/**
 * Common {@link EntityDefinition entity definitions} for electric sensors.
 *
 * @author Morten Hermansen
 */
public class ElectricSensorDevice {

  /** A {@link SensorEntity} definition for reporting meassured current. */
  public static final EntityDefinition<SensorEntity<Double>> CURRENT =
      new EntityDefinition<>("Electric current", (em, id) -> em.decimalSensorEntity(id));

  /** A {@link SensorEntity} definition for reporting meassured power. */
  public static final EntityDefinition<SensorEntity<Double>> POWER =
      new EntityDefinition<>("Electric power", (em, id) -> em.decimalSensorEntity(id));

  /** A {@link SensorEntity} definition for reporting meassured voltage. */
  public static final EntityDefinition<SensorEntity<Double>> VOLTAGE =
      new EntityDefinition<>("Voltage", (em, id) -> em.decimalSensorEntity(id));
}
