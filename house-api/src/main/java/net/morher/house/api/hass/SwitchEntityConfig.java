package net.morher.house.api.hass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
public class SwitchEntityConfig extends BaseEntityConfig {
    @JsonProperty("cmd_t")
    public String commandTopic;

    @JsonProperty("stat_t")
    public String stateTopic;

    @Override
    public String getEntityClass() {
        return "switch";
    }

}
