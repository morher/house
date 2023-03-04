package net.morher.house.api.hass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.morher.house.api.entity.cover.CoverEntity;
import net.morher.house.api.entity.cover.CoverOptions;
import net.morher.house.api.mqtt.client.HouseMqttClient;

public class CoverEntityAnnouncer extends BaseEntityAnnouncer<CoverEntity> {

  public CoverEntityAnnouncer(HouseMqttClient mqtt) {
    super(mqtt, CoverEntity.class);
  }

  @Override
  protected void announceEntity(CoverEntity entity) {
    CoverOptions options = entity.getOptions();
    if (options != null) {
      CoverEntityConfig entityConfig = new CoverEntityConfig();
      fillDefaults(entity, entityConfig);

      entityConfig.setStateTopic(entity.state().getTopic());
      entityConfig.setCommandTopic(entity.command().getTopic());

      if (options.isPosition()) {
        entityConfig.setPositionTopic(entity.position().state().getTopic());
        entityConfig.setPositionCommandTopic(entity.position().command().getTopic());
      }

      if (options.isTilt()) {
        entityConfig.setTiltTopic(entity.tilt().state().getTopic());
        entityConfig.setTiltCommandTopic(entity.tilt().command().getTopic());
      }

      if (options.isInvertDirection()) {
        entityConfig.setPositionOpen(0);
        entityConfig.setPositionClosed(100);
      }

      announceEntity(entityConfig);
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  @JsonInclude(Include.NON_NULL)
  public static class CoverEntityConfig extends BaseEntityConfig {
    @JsonProperty("cmd_t")
    private String commandTopic;

    @JsonProperty("stat_t")
    private String stateTopic;

    @JsonProperty("pos_t")
    private String positionTopic;

    @JsonProperty("set_pos_t")
    private String positionCommandTopic;

    @JsonProperty("tilt_t")
    private String tiltTopic;

    @JsonProperty("tilt_cmd_t")
    private String tiltCommandTopic;

    @JsonProperty("position_closed")
    private int positionClosed = 0;

    @JsonProperty("position_open")
    private int positionOpen = 100;

    @Override
    public String getEntityClass() {
      return "cover";
    }
  }
}
