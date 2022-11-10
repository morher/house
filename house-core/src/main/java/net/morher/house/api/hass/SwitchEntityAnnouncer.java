package net.morher.house.api.hass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
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

            entityConfig.setStateTopic(entity.state().getTopic());
            entityConfig.setCommandTopic(entity.command().getTopic());
            announceEntity(entityConfig);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @JsonInclude(Include.NON_NULL)
    public static class SwitchEntityConfig extends BaseEntityConfig {
        @JsonProperty("cmd_t")
        public String commandTopic;

        @JsonProperty("stat_t")
        public String stateTopic;

        @Override
        public String getEntityClass() {
            return "switch";
        }

    }
}
