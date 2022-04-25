package net.morher.house.api.entity.light;

import net.morher.house.api.entity.CommandableEntity;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.payload.JsonMessage;
import net.morher.house.api.mqtt.payload.PayloadFormat;

public class LightEntity extends CommandableEntity<LightState, LightOptions, LightState> {

    private static final PayloadFormat<LightState> LIGHT_STATE_CONVERTER = JsonMessage.toType(LightState.class);

    public LightEntity(HouseMqttClient client, EntityId entityId, EntityListener entityListener) {
        super(client, entityId, entityListener, LIGHT_STATE_CONVERTER, LIGHT_STATE_CONVERTER);
    }

}
