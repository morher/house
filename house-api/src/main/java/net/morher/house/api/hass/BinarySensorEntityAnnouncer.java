package net.morher.house.api.hass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.morher.house.api.entity.sensor.BinarySensorEntity;
import net.morher.house.api.entity.sensor.BinarySensorOptions;
import net.morher.house.api.mqtt.client.HouseMqttClient;

public class BinarySensorEntityAnnouncer extends BaseEntityAnnouncer<BinarySensorEntity> {

    public BinarySensorEntityAnnouncer(HouseMqttClient mqtt) {
        super(mqtt, BinarySensorEntity.class);
    }

    @Override
    protected void announceEntity(BinarySensorEntity entity) {
        BinarySensorEntityConfig entityConfig = new BinarySensorEntityConfig();
        fillDefaults(entity, entityConfig);

        BinarySensorOptions options = entity.getOptions();
        if (options != null) {
            entityConfig.setDeviceClass(options.getDeviceClass());
            if (options.getCategory() != null) {
                entityConfig.setEntityCategory(options.getCategory().name().toLowerCase());
            }
        }

        entityConfig.setStateTopic(entity.state().getTopic());

        announceEntity(entityConfig);
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BinarySensorEntityConfig extends BaseEntityConfig {

        @JsonProperty("dev_cla")
        private String deviceClass;

        @JsonProperty("stat_t")
        public String stateTopic;

        @Override
        public String getEntityClass() {
            return "binary_sensor";
        }
    }

}
