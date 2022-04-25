package net.morher.house.api.device;

import net.morher.house.api.entity.Entity;
import net.morher.house.api.entity.EntityManager;
import net.morher.house.api.entity.light.LightEntity;

public class DeviceManager {
    private final EntityManager entityManager;

    public DeviceManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <M extends Entity> Device<M> device(DeviceId deviceId, EntityFactory<? extends M> entityFactory) {
        return new Device<>(entityManager, deviceId, entityFactory);
    }

    public Device<LightEntity> lightDevice(DeviceId deviceId) {
        return device(deviceId, (em, id) -> em.lightEntity(id));
    }
}
