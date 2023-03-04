package net.morher.house.api.mqtt;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.EntityId;

public interface MqttNamespace {

  public static MqttNamespace defaultNamespace() {
    return new DefaultMqttNamespace();
  }

  String clientAvailabilityTopic(String clientName);

  String getDeviceBaseTopic(DeviceId id);

  String entityBaseTopic(EntityId entityId);

  String entityCommandTopic(EntityId entityId);

  String entityPositionTopic(EntityId entityId);

  String entityPositionCommandTopic(EntityId entityId);

  String entityStateTopic(EntityId entityId);

  String entityTiltTopic(EntityId entityId);

  String entityTiltCommandTopic(EntityId entityId);

  String entityTriggerTopic(EntityId entityId);

  public static String normalize(String name) {
    return Normalizer.normalize(name, Form.NFD) // Separates letters and accents
        .replaceAll("\\p{M}", "") // Removes accents
        .toLowerCase()
        .replace("æ", "ae")
        .replace("ø", "o")
        .replaceAll("-", "") // Remove hyphens
        .replaceAll("[^a-z0-9_]+", "-") // Replace any unknown letter or number with -.
        .replaceAll("^-", "") // Remove leading dash
        .replaceAll("-$", ""); // Remove trailing dash
  }
}
