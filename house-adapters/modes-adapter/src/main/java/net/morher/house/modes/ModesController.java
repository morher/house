package net.morher.house.modes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.morher.house.api.device.DeviceId;
import net.morher.house.api.device.DeviceInfo;
import net.morher.house.api.entity.CommandableEntity;
import net.morher.house.api.entity.EntityCommandListener;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityManager;
import net.morher.house.api.entity.EntityOptions;
import net.morher.house.api.entity.switches.SwitchOptions;
import net.morher.house.modes.ModesAdapterConfiguration.ModeDeviceConfiguration;
import net.morher.house.modes.ModesAdapterConfiguration.ModeEntityConfiguration;
import net.morher.house.modes.ModesAdapterConfiguration.ModesConfiguration;

public class ModesController {
    private final EntityManager entityManager;

    public ModesController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void configure(ModesConfiguration config) {
        for (ModeDeviceConfiguration deviceConfig : config.getDevices()) {
            configure(deviceConfig);
        }
    }

    private void configure(ModeDeviceConfiguration deviceConfig) {
        DeviceId deviceId = deviceConfig.getDevice().toDeviceId();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setManufacturer(deviceConfig.getManufacturer());
        deviceInfo.setModel(deviceConfig.getModel());
        deviceInfo.setConfigurationUrl(deviceConfig.getConfigurationUrl());
        deviceInfo.setSwVersion(deviceConfig.getSwVersion());

        ModesDevice device = new ModesDevice(deviceId, deviceInfo);

        ModeEntityConfiguration mainEntityConfig = deviceConfig.getMainEntity();
        if (mainEntityConfig != null) {
            configureEntity(mainEntityConfig, device, null);
        }
        for (Entry<String, ModeEntityConfiguration> entity : deviceConfig.getEntities().entrySet()) {
            configureEntity(entity.getValue(), device, entity.getKey());
        }
    }

    private void configureEntity(ModeEntityConfiguration entityConfig, ModesDevice device, String entityName) {
        device.getEntities().add(createEntity(entityConfig, device, entityName));
    }

    private ModesEntity createEntity(ModeEntityConfiguration entityConfig, ModesDevice device, String entityName) {
        switch (entityConfig.getType()) {
        case "switch":
            SwitchOptions options = new SwitchOptions();
            options.setIcon(entityConfig.getIcon());
            return new ModesPassthroughEntity<>(
                    entityManager.switchEntity(device.entityId(entityName)),
                    device.getDeviceInfo(),
                    options);

        }
        throw new IllegalArgumentException("Unknown entity type: " + entityConfig.getType());
    }

    private interface ModesEntity {

    }

    private static class ModesPassthroughEntity<P, O extends EntityOptions, E extends CommandableEntity<P, O, P>>
            implements ModesEntity, EntityCommandListener<P> {
        private final E entity;

        public ModesPassthroughEntity(E entity, DeviceInfo deviceInfo, O options) {
            this.entity = entity;
            entity.command().subscribe(this);
            entity.setDeviceInfo(deviceInfo);
            entity.setOptions(options);
        }

        @Override
        public void onCommand(P command) {
            entity.publishState(command);
        }
    }

    @AllArgsConstructor
    @Getter
    public static class ModesDevice {
        private final DeviceId deviceId;
        private final DeviceInfo deviceInfo;
        private final List<ModesEntity> entities = new ArrayList<>();

        public EntityId entityId(String entityName) {
            return new EntityId(deviceId, entityName);
        }
    }
}
