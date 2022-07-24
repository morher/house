package net.morher.house.api.hass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.morher.house.api.entity.cover.CoverEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;

public class CoverEntityAnnouncer extends BaseEntityAnnouncer<CoverEntity> {

    public CoverEntityAnnouncer(HouseMqttClient mqtt) {
        super(mqtt, CoverEntity.class);
    }

    @Override
    protected void announceEntity(CoverEntity entity) {
        if (entity.getOptions() != null) {
            CoverEntityConfig entityConfig = new CoverEntityConfig();
            fillDefaults(entity, entityConfig);

            entityConfig.setStateTopic(entity.getStateTopic());
            entityConfig.setCommandTopic(entity.getCommandTopic());
            announceEntity(entityConfig);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @JsonInclude(Include.NON_NULL)
    public static class CoverEntityConfig extends BaseEntityConfig {
        @JsonProperty("cmd_t")
        public String commandTopic;

        @JsonProperty("stat_t")
        public String stateTopic;

        @Override
        public String getEntityClass() {
            return "cover";
        }

    }
}
