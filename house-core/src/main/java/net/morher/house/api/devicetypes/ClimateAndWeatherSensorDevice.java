package net.morher.house.api.devicetypes;

import net.morher.house.api.entity.EntityDefinition;
import net.morher.house.api.entity.EntityManager;
import net.morher.house.api.entity.sensor.SensorEntity;

public class ClimateAndWeatherSensorDevice {
  /** A {@link SensorEntity} definition for reporting humidity. */
  public static final EntityDefinition<SensorEntity<Double>> HUMIDITY =
      new EntityDefinition<>("Humidity", EntityManager::decimalSensorEntity);

  /** A {@link SensorEntity} definition for reporting temperature. */
  public static final EntityDefinition<SensorEntity<Double>> ILLUMINANCE =
      new EntityDefinition<>("Illuminance", EntityManager::decimalSensorEntity);

  /** A {@link SensorEntity} definition for reporting temperature. */
  public static final EntityDefinition<SensorEntity<Double>> TEMPERATURE =
      new EntityDefinition<>("Temperature", EntityManager::decimalSensorEntity);
}
