package net.morher.house.api.entity.common;

import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.Topic;
import net.morher.house.api.mqtt.payload.PayloadFormat;

public abstract class CommandableEntity<S, O extends EntityOptions, C>
    extends StatefullEntity<S, O> {
  private final Topic<C> commandTopic;

  public CommandableEntity(
      HouseMqttClient client,
      EntityId entityId,
      EntityListener entityListener,
      PayloadFormat<S> stateSerializer,
      PayloadFormat<C> commandSerializer) {
    super(client, entityId, entityListener, stateSerializer);

    this.commandTopic =
        client.topic(client.getNamespace().entityCommandTopic(entityId), commandSerializer, false);
  }

  public String getCommandTopic() {
    return this.commandTopic.getTopic();
  }

  public void sendCommand(C command) {
    commandTopic.publish(command);
  }

  public final Topic<C> command() {
    return commandTopic;
  }
}
