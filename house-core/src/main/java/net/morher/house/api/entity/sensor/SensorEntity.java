package net.morher.house.api.entity.sensor;

import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.entity.common.StatefullEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.payload.PayloadFormat;

public class SensorEntity<S> extends StatefullEntity<S, SensorOptions> {

  public SensorEntity(
      HouseMqttClient client,
      EntityId entityId,
      EntityListener entityListener,
      PayloadFormat<S> sensorFormat) {
    super(client, entityId, entityListener, sensorFormat);
  }
}
