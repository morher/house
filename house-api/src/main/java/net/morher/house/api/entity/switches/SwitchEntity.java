package net.morher.house.api.entity.switches;

import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.entity.common.CommandableEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.payload.BooleanMessage;

public class SwitchEntity extends CommandableEntity<Boolean, SwitchOptions, Boolean> {

    public SwitchEntity(HouseMqttClient client, EntityId entityId, EntityListener entityListener) {
        super(client, entityId, entityListener, BooleanMessage.onOff(), BooleanMessage.onOff());
    }

}
