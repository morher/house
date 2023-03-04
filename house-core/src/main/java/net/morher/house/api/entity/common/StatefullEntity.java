package net.morher.house.api.entity.common;

import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.Topic;
import net.morher.house.api.mqtt.payload.PayloadFormat;

public class StatefullEntity<S, O extends EntityOptions> extends ConfigurableEntity<O> {
  protected final Topic<S> stateTopic;
  protected S currentState;

  public StatefullEntity(
      HouseMqttClient client,
      EntityId entityId,
      EntityListener entityListener,
      PayloadFormat<S> stateSerializer) {
    super(entityId, entityListener);

    this.stateTopic =
        client.topic(client.getNamespace().entityStateTopic(entityId), stateSerializer, true);
  }

  public Topic<S> state() {
    return stateTopic;
  }
}
