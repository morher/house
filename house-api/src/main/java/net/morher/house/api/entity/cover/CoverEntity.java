package net.morher.house.api.entity.cover;

import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.entity.common.CommandableEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.payload.EnumMessage;
import net.morher.house.api.mqtt.payload.PayloadFormat;

public class CoverEntity extends CommandableEntity<CoverState, CoverOptions, CoverCommand> {
    private static final PayloadFormat<CoverState> STATE_FORMAT = EnumMessage.lowercase(CoverState.class);
    private static final PayloadFormat<CoverCommand> COMMAND_FORMAT = EnumMessage.lowercase(CoverCommand.class);

    public CoverEntity(HouseMqttClient client, EntityId entityId, EntityListener entityListener) {
        super(client, entityId, entityListener, STATE_FORMAT, COMMAND_FORMAT);
    }

}
