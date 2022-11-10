package net.morher.house.api.devicetypes;

import net.morher.house.api.entity.EntityDefinition;
import net.morher.house.api.entity.cover.CoverEntity;

public class CoverDevice {

    /**
     * <p>
     * A {@link CoverEntity} definition for covers.
     * 
     * <p>
     * Examples: Blinds, awnings, projector screens.
     */
    public static final EntityDefinition<CoverEntity> COVER = new EntityDefinition<>("Cover", (em, id) -> em.coverEntity(id));

}
