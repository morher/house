package net.morher.house.api.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.morher.house.api.entity.Entity;
import net.morher.house.api.entity.EntityManager;
import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.entity.switches.SwitchEntity;

@AllArgsConstructor
@Getter
public class DeviceManager {
    private final EntityManager entityManager;

    public <M extends Entity> Device<M> device(DeviceId deviceId, EntityFactory<? extends M> entityFactory) {
        return new Device<>(entityManager, deviceId, entityFactory);
    }

    public Device<LightEntity> lightDevice(DeviceId deviceId) {
        return device(deviceId, (em, id) -> em.lightEntity(id));
    }

    public Device<SwitchEntity> switchDevice(DeviceId deviceId) {
        return device(deviceId, (em, id) -> em.switchEntity(id));
    }
}
