package net.morher.house.api.entity.number;

import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.entity.common.CommandableEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.payload.NumberMessage;

public class DecimalEntity extends CommandableEntity<Double, NumberOptions, Double> {

  public DecimalEntity(HouseMqttClient client, EntityId entityId, EntityListener entityListener) {
    super(client, entityId, entityListener, NumberMessage.decimal(), NumberMessage.decimal());
  }
}
