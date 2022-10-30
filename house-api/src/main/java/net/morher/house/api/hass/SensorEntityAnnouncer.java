package net.morher.house.api.hass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.morher.house.api.entity.sensor.SensorEntity;
import net.morher.house.api.entity.sensor.SensorOptions;
import net.morher.house.api.mqtt.client.HouseMqttClient;

@SuppressWarnings("rawtypes")
public class SensorEntityAnnouncer extends BaseEntityAnnouncer<SensorEntity<?>> {

    @SuppressWarnings("unchecked")
    public SensorEntityAnnouncer(HouseMqttClient mqtt) {
        super(mqtt, (Class<SensorEntity<?>>) (Class) SensorEntity.class);
    }

    @Override
    protected void announceEntity(SensorEntity rawEntity) {
        SensorEntity<?> entity = (SensorEntity<?>) rawEntity;
        SensorEntityConfig entityConfig = new SensorEntityConfig();
        fillDefaults(entity, entityConfig);

        SensorOptions options = entity.getOptions();
        if (options != null) {
            entityConfig.setDeviceClass(options.getDeviceClass());
            entityConfig.setUnit(options.getUnit());
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
    public static class SensorEntityConfig extends BaseEntityConfig {

        @JsonProperty("dev_cla")
        private String deviceClass;

        @JsonProperty("unit_of_measurement")
        private String unit;

        @JsonProperty("stat_t")
        public String stateTopic;

        @Override
        public String getEntityClass() {
            return "sensor";
        }
    }

}
