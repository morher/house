package net.morher.house.api.devicetypes;

import net.morher.house.api.entity.EntityDefinition;
import net.morher.house.api.entity.sensor.SensorEntity;
import net.morher.house.api.entity.switches.SwitchDefinition;
import net.morher.house.api.entity.switches.SwitchEntity;
import net.morher.house.api.entity.trigger.TriggerEntity;

/**
 * Common {@link EntityDefinition entity definitions}.
 *
 * @author Morten Hermansen
 */
public class GeneralDevice {

  /**
   * A {@link TriggerEntity} definition for triggers such as button presses and motion detection.
   */
  public static final EntityDefinition<TriggerEntity> CONTROL =
      new EntityDefinition<>("Control", (em, id) -> em.triggerEntity(id));

  /**
   * A {@link SensorEntity} definition for reporting device battery level. The battery level should
   * be reported as a value between 0 (empty) and 100 (full).
   *
   * <p>Examples: wireless sensors, wireless buttons.
   */
  public static final EntityDefinition<SensorEntity<Double>> DEVICE_BATTERY =
      new EntityDefinition<>("Device battery", (em, id) -> em.decimalSensorEntity(id));

  /** A {@link SensorEntity} definition for reporting the internal device temperature. */
  public static final EntityDefinition<SensorEntity<Double>> DEVICE_TEMPERATURE =
      new EntityDefinition<>("Device temperature", (em, id) -> em.decimalSensorEntity(id));

  /**
   * A {@link SwitchEntity} definition for devices that can have their functionality enabled or
   * disabled, while it doesn't necessarily change the power status. It can also be used for virtual
   * devices representing a binary state, rather than turning something physical on or off.
   *
   * <p>Examples: Guest mode, vacation mode, motion detection for light automation.
   */
  public static final EntityDefinition<SwitchEntity> ENABLE = new SwitchDefinition("Enable");

  /**
   * A {@link SwitchEntity} definition for devices than can be turned on or off, typically using
   * power.
   *
   * <p>Examples: fans, coffee machines, controlled outlets and TVs.
   */
  public static final EntityDefinition<SwitchEntity> POWER = new SwitchDefinition("Power");

  /**
   * A {@link SensorEntity} definition for the device status. This entity is for reporting an
   * overall status of a device, more than just whether it is on or off.
   *
   * <p>Examples: Battery charger state, Dishwasher status.
   */
  public static final EntityDefinition<SensorEntity<String>> STATUS =
      new EntityDefinition<>("Status", (em, id) -> em.stringSensorEntity(id));
}
