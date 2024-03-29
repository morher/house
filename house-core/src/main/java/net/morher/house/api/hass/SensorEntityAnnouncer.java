package net.morher.house.api.hass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.morher.house.api.entity.sensor.SensorEntity;
import net.morher.house.api.entity.sensor.SensorOptions;
import net.morher.house.api.mqtt.client.HouseMqttClient;

@SuppressWarnings("rawtypes")
public class SensorEntityAnnouncer extends BaseEntityAnnouncer<SensorEntity<?>> {
  private final Map<String, String> unitMapping = new HashMap<>();

  @SuppressWarnings("unchecked")
  public SensorEntityAnnouncer(HouseMqttClient mqtt) {
    super(mqtt, (Class<SensorEntity<?>>) (Class) SensorEntity.class);
    unitMapping.put("seconds", "s");
    unitMapping.put("minutes", "min");
    unitMapping.put("hours", "h");
    unitMapping.put("days", "d");
  }

  @Override
  protected void announceEntity(SensorEntity rawEntity) {
    SensorEntity<?> entity = (SensorEntity<?>) rawEntity;
    SensorOptions options = entity.getOptions();
    if (options != null) {
      SensorEntityConfig entityConfig = new SensorEntityConfig();
      fillDefaults(entity, entityConfig);

      entityConfig.setDeviceClass(options.getDeviceClass());
      entityConfig.setUnit(unit(options.getUnit()));
      if (options.getCategory() != null) {
        entityConfig.setEntityCategory(options.getCategory().name().toLowerCase());
      }

      entityConfig.setStateTopic(entity.state().getTopic());

      announceEntity(entityConfig);
    }
  }

  private String unit(String unit) {
    return unitMapping.getOrDefault(unit, unit);
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
