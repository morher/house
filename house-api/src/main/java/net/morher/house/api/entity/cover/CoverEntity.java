package net.morher.house.api.entity.cover;

import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.entity.common.CommandableEntity;
import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.StateCommandPair;
import net.morher.house.api.mqtt.payload.EnumMessage;
import net.morher.house.api.mqtt.payload.NumberMessage;
import net.morher.house.api.mqtt.payload.PayloadFormat;

public class CoverEntity extends CommandableEntity<CoverState, CoverOptions, CoverCommand> {
    private static final PayloadFormat<CoverState> STATE_FORMAT = EnumMessage.lowercase(CoverState.class);
    private static final PayloadFormat<CoverCommand> COMMAND_FORMAT = EnumMessage.lowercase(CoverCommand.class);

    private final StateCommandPair position;
    private final StateCommandPair tilt;

    public CoverEntity(HouseMqttClient client, EntityId entityId, EntityListener entityListener) {
        super(client, entityId, entityListener, STATE_FORMAT, COMMAND_FORMAT);

        MqttNamespace namespace = client.getNamespace();
        position = new StateCommandPair(
                client.topic(namespace.entityPositionTopic(entityId), NumberMessage.integer(), true),
                client.topic(namespace.entityPositionCommandTopic(entityId), NumberMessage.integer(), false));
        tilt = new StateCommandPair(
                client.topic(namespace.entityTiltTopic(entityId), NumberMessage.integer(), true),
                client.topic(namespace.entityTiltCommandTopic(entityId), NumberMessage.integer(), false));
    }

    public StateCommandPair position() {
        return position;
    }

    public StateCommandPair tilt() {
        return tilt;
    }
}
