package net.morher.house.api.entity.switches;

import net.morher.house.api.entity.EntityDefinition;
import net.morher.house.api.entity.EntityFactory;

public class SwitchDefinition extends EntityDefinition<SwitchEntity> {
    public static final EntityFactory<SwitchEntity> ENTITY_FACTORY = (em, id) -> em.switchEntity(id);

    public SwitchDefinition(String entityName) {
        super(entityName, ENTITY_FACTORY);
    }
}
