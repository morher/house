package net.morher.house.api.entity;

import lombok.Getter;

public class ConfigurableEntity<O extends EntityOptions> extends Entity {
    @Getter
    private O options;

    public ConfigurableEntity(EntityId id, EntityListener entityListener) {
        super(id, entityListener);
    }

    public void setOptions(O options) {
        this.options = options;
        onEntityUpdated();
    }
}