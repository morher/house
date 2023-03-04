package net.morher.house.api.devicetypes;

import net.morher.house.api.entity.EntityDefinition;
import net.morher.house.api.entity.number.DecimalEntity;
import net.morher.house.api.entity.sensor.SensorEntity;
import net.morher.house.api.entity.switches.SwitchEntity;

public class AudioVideoDevice {

  /**
   * A {@link SwitchEntity} definition for devices where the audio and/or video can be muted.
   *
   * <p>Examples: TVs, Monitors and projectors.
   */
  public static final EntityDefinition<SwitchEntity> AV_MUTE =
      new EntityDefinition<>("AV-mute", (em, id) -> em.switchEntity(id));

  /**
   * A {@link SensorEntity} definition for projectors and other devices where reporting the current
   * use of the lamp makes sense.
   */
  public static final EntityDefinition<SensorEntity<Integer>> LAMP_HOURS =
      new EntityDefinition<>("Lamp hours", (em, id) -> em.integerSensorEntity(id));

  /**
   * A {@link DecimalEntity} definition for devices where the audio volume can be adjusted.
   *
   * <p>Examples: TVs, Monitors and projectors.
   */
  public static final EntityDefinition<DecimalEntity> VOLUME =
      new EntityDefinition<>("Volume", (em, id) -> em.decimalEntity(id));
}
