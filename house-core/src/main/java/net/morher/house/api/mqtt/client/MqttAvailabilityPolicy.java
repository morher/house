package net.morher.house.api.mqtt.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MqttAvailabilityPolicy {
  private final String topic;
  private final byte[] onlinePayload;
  private final byte[] offlinePayload;
  private final int qos;
  private final boolean retain;

  public MqttAvailabilityPolicy(String topic) {
    this(topic, "online".getBytes(), "offline".getBytes(), 0, true);
  }
}
