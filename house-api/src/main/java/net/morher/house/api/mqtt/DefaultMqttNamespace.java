package net.morher.house.api.mqtt;

import static java.util.Objects.requireNonNullElse;
import static net.morher.house.api.mqtt.MqttNamespace.normalize;

import lombok.AllArgsConstructor;
import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.EntityId;

@AllArgsConstructor
public class DefaultMqttNamespace implements MqttNamespace {
  private final String houseName;
  private final String defaultRoomName;

  public DefaultMqttNamespace() {
    this("house", "no-room");
  }

  @Override
  public String clientAvailabilityTopic(String clientName) {
    return "service/" + clientName + "/available";
  }

  public String getDeviceBaseTopic(DeviceId id) {
    return houseName
        + "/"
        + normalize(requireNonNullElse(id.getRoomName(), defaultRoomName))
        + "/"
        + normalize(id.getDeviceName());
  }

  public String entityBaseTopic(EntityId entityId) {
    String deviceBaseTopic = getDeviceBaseTopic(entityId.getDevice());
    String entityName = entityId.getEntity();
    if (entityName != null) {
      return deviceBaseTopic + "/" + normalize(entityName);
    }
    return deviceBaseTopic;
  }

  public String entityCommandTopic(EntityId entity) {
    return entityBaseTopic(entity) + "/command";
  }

  @Override
  public String entityPositionTopic(EntityId entity) {
    return entityBaseTopic(entity) + "/position";
  }

  @Override
  public String entityPositionCommandTopic(EntityId entity) {
    return entityBaseTopic(entity) + "/position/command";
  }

  public String entityStateTopic(EntityId entityId) {
    return entityBaseTopic(entityId);
  }

  @Override
  public String entityTiltTopic(EntityId entity) {
    return entityBaseTopic(entity) + "/tilt";
  }

  @Override
  public String entityTiltCommandTopic(EntityId entity) {
    return entityBaseTopic(entity) + "/tilt/command";
  }

  @Override
  public String entityTriggerTopic(EntityId entityId) {
    return entityBaseTopic(entityId);
  }
}
