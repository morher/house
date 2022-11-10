package net.morher.house.api.hass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.morher.house.api.entity.trigger.TriggerEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;

public class TriggerEntityAnnouncer extends BaseEntityAnnouncer<TriggerEntity> {

    public TriggerEntityAnnouncer(HouseMqttClient mqtt) {
        super(mqtt, TriggerEntity.class);
    }

    @Override
    protected void announceEntity(TriggerEntity entity) {
        if (entity.getOptions() != null) {

            for (String event : entity.getOptions().getAvailableEvents()) {
                TriggerEntityConfig entityConfig = new TriggerEntityConfig();
                fillDefaults(entity, entityConfig);

                entityConfig.setUniqueId(entityConfig.getUniqueId() + "/" + event);

                entityConfig.setTopic(entity.getTriggerTopic());
                entityConfig.setPayload(event);
                entityConfig.setType(event);
                entityConfig.setSubtype(event);

                announceEntity(entityConfig);
            }
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @JsonInclude(Include.NON_NULL)
    public static class TriggerEntityConfig extends BaseEntityConfig {

        @JsonProperty("automation_type")
        public String automationType = "trigger";

        @JsonProperty("topic")
        public String topic;

        public String payload;

        public String type;

        public String subtype;

        @Override
        public String getEntityClass() {
            return "device_automation";
        }

    }
}
