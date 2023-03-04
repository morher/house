package net.morher.house.api.hass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    entityConfig.setStateTopic(entity.state().getTopic());
    entityConfig.setCommandTopic(entity.command().getTopic());

    announceEntity(entityConfig);
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  @JsonInclude(Include.NON_NULL)
  public static class LightEntityConfig extends BaseEntityConfig {

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

    @Override
    public String getEntityClass() {
      return "light";
    }
  }
}
