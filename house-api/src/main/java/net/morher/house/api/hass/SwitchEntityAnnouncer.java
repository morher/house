package net.morher.house.api.hass;

import net.morher.house.api.entity.switches.SwitchEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;

public class SwitchEntityAnnouncer extends BaseEntityAnnouncer<SwitchEntity> {

    public SwitchEntityAnnouncer(HouseMqttClient mqtt) {
        super(mqtt, SwitchEntity.class);
    }

    @Override
    protected void announceEntity(SwitchEntity entity) {
        if (entity.getOptions() != null) {
            SwitchEntityConfig entityConfig = new SwitchEntityConfig();
            fillDefaults(entity, entityConfig);

            entityConfig.setStateTopic(entity.getStateTopic());
            entityConfig.setCommandTopic(entity.getCommandTopic());
            announceEntity(entityConfig);
        }
    }
}
