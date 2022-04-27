package net.morher.house.api.hass;

import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.entity.light.LightOptions;
import net.morher.house.api.mqtt.client.HouseMqttClient;

public class LightEntityAnnouncer extends BaseEntityAnnouncer<LightEntity> {

    public LightEntityAnnouncer(HouseMqttClient mqtt) {
        super(mqtt, LightEntity.class);
    }

    @Override
    protected void announceEntity(LightEntity entity) {
        LightEntityConfig entityConfig = new LightEntityConfig();
        fillDefaults(entity, entityConfig);

        LightOptions options = entity.getOptions();
        if (options != null) {
            entityConfig.brightness = options.isDimmable();
            entityConfig.effectList = options.getEffects();
            entityConfig.effect = entityConfig.effectList != null && !entityConfig.effectList.isEmpty();
        }
        entityConfig.setStateTopic(entity.getStateTopic());
        entityConfig.setCommandTopic(entity.getCommandTopic());

        announceEntity(entityConfig);
    }
}
