package net.morher.house.api.hass;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class LightEntityConfig extends BaseEntityConfig {

    @JsonProperty("~")
    public String baseTopic;

    @JsonProperty("cmd_t")
    public String commandTopic;

    @JsonProperty("stat_t")
    public String stateTopic;

    public String schema = "json";

    public boolean brightness;

    public boolean effect;

    @JsonProperty("effect_list")
    public List<String> effectList = new ArrayList<String>();

    public String getBaseTopic() {
        return baseTopic;
    }

    public void setBaseTopic(String baseTopic) {
        this.baseTopic = baseTopic;
    }

    public String getCommandTopic() {
        return commandTopic;
    }

    public void setCommandTopic(String commandTopic) {
        this.commandTopic = commandTopic;
    }

    public String getStateTopic() {
        return stateTopic;
    }

    public void setStateTopic(String stateTopic) {
        this.stateTopic = stateTopic;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public boolean isBrightness() {
        return brightness;
    }

    public void setBrightness(boolean brightness) {
        this.brightness = brightness;
    }

    public boolean isEffect() {
        return effect;
    }

    public void setEffect(boolean effect) {
        this.effect = effect;
    }

    public List<String> getEffectList() {
        return effectList;
    }

    @Override
    public String getEntityClass() {
        return "light";
    }
}
