package net.morher.house.api.devicetypes;

import net.morher.house.api.entity.EntityDefinition;
import net.morher.house.api.entity.sensor.BinarySensorEntity;
import net.morher.house.api.entity.sensor.SensorEntity;

/**
 * Common {@link EntityDefinition entity definitions}.
 *
 * @author Morten Hermansen
 */
public class RoomSensorDevice {

  /** A {@link SensorEntity} definition for devices detecting moisture. */
  public static final EntityDefinition<BinarySensorEntity> MOISTURE =
      new EntityDefinition<>("Moisture", (em, id) -> em.binarySensorEntity(id));

  /** A {@link SensorEntity} definition for devices reporting movement in a room or area. */
  public static final EntityDefinition<BinarySensorEntity> MOTION =
      new EntityDefinition<>("Motion", (em, id) -> em.binarySensorEntity(id));

  /**
   * A {@link BinarySensorEntity} definition for devices reporting room opening states, such as
   * doors and windows.
   *
   * <p>When the door/window is open, the binary sensor reports ON.
   */
  public static final EntityDefinition<BinarySensorEntity> OPENING =
      new EntityDefinition<>("Opening", (em, id) -> em.binarySensorEntity(id));

  /** A {@link SensorEntity} definition for devices reporting room presence. */
  public static final EntityDefinition<BinarySensorEntity> PRESENCE =
      new EntityDefinition<>("Presence", (em, id) -> em.binarySensorEntity(id));

  /** A {@link SensorEntity} definition for devices detectin smoke. */
  public static final EntityDefinition<BinarySensorEntity> SMOKE =
      new EntityDefinition<>("Smoke", (em, id) -> em.binarySensorEntity(id));

  /** A {@link SensorEntity} definition for devices reporting tilt degrees for i.e. windows. */
  public static final EntityDefinition<SensorEntity<Double>> TILT =
      new EntityDefinition<>("Tilt", (em, id) -> em.decimalSensorEntity(id));

  /** A {@link SensorEntity} definition for devices detecting moisture. */
  public static final EntityDefinition<BinarySensorEntity> VIBRATION =
      new EntityDefinition<>("Vibration", (em, id) -> em.binarySensorEntity(id));
}
