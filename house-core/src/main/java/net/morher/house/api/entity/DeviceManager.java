package net.morher.house.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeviceManager {
  private final EntityManager entityManager;

  public <M extends Entity> Device device(DeviceId deviceId) {
    return new Device(entityManager, deviceId);
  }
}
