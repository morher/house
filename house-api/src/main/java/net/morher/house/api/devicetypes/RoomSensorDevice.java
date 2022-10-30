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

    /**
     * <p>
     * A {@link SensorEntity} definition for devices reporting movement in a room or area.
     */
    public static final EntityDefinition<BinarySensorEntity> MOTION = new EntityDefinition<>("Motion", (em, id) -> em.binarySensorEntity(id));

    /**
     * <p>
     * A {@link SensorEntity} definition for devices reporting room presence.
     */
    public static final EntityDefinition<BinarySensorEntity> PRESENCE = new EntityDefinition<>("Presence", (em, id) -> em.binarySensorEntity(id));

}
