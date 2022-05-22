package net.morher.house.api.devicetypes;

import net.morher.house.api.entity.EntityDefinition;
import net.morher.house.api.entity.switches.SwitchEntity;

/**
 * Common {@link EntityDefinition entity definitions}.
 * 
 * @author Morten Hermansen
 */
public class GeneralDevice {
    /**
     * <p>
     * A {@link SwitchEntity} definition for devices than can be turned on or off, typically using power.
     * 
     * <p>
     * Examples: fans, coffee machines, controlled outlets and TVs.
     */
    public static final EntityDefinition<SwitchEntity> POWER = new EntityDefinition<>("Power", (em, id) -> em.switchEntity(id));

    /**
     * <p>
     * A {@link SwitchEntity} definition for devices that can have their functionality enabled or disabled, while it doesn't
     * necessarily change the power status. It can also be used for virtual devices representing a binary state, rather than
     * turning something physical on or off.
     * 
     * <p>
     * Examples: Guest mode, vacation mode, motion detection for light automation.
     */
    public static final EntityDefinition<SwitchEntity> ENABLE = new EntityDefinition<>("Enable", (em, id) -> em.switchEntity(id));

}
