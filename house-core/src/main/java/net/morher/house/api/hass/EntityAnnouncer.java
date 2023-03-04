package net.morher.house.api.hass;

import net.morher.house.api.entity.Entity;

public interface EntityAnnouncer {
  void announce(Entity entity);
}
