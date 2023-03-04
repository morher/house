package net.morher.house.api.entity.common;

import lombok.Getter;
import net.morher.house.api.entity.Entity;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;

public class ConfigurableEntity<O extends EntityOptions> extends Entity {
  @Getter private O options;

  public ConfigurableEntity(EntityId id, EntityListener entityListener) {
    super(id, entityListener);
  }

  public void setOptions(O options) {
    this.options = options;
    onEntityUpdated();
  }
}
