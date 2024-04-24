package net.morher.house.api.entity.sound;

import lombok.Getter;
import net.morher.house.api.entity.Entity;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.Topic;
import net.morher.house.api.mqtt.payload.JsonMessage;

public class SoundEntity extends Entity {
  @Getter private final Topic<SoundCommand> soundRequestTopic;

  public SoundEntity(HouseMqttClient client, EntityId id, EntityListener entityListener) {
    super(id, entityListener);
    this.soundRequestTopic =
        client.topic(
            client.getNamespace().entitySoundRequestTopic(id),
            JsonMessage.toType(SoundCommand.class));
  }

  public void sendSoundRequest(SoundCommand command) {
    soundRequestTopic.publish(command);
  }

  public final Topic<SoundCommand> soundRequest() {
    return soundRequestTopic;
  }
}
