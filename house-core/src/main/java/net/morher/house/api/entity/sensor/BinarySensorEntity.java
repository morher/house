package net.morher.house.api.entity.sensor;

import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.entity.common.StatefullEntity;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.payload.PayloadFormat;

public class BinarySensorEntity extends StatefullEntity<Boolean, BinarySensorOptions> {

    public BinarySensorEntity(HouseMqttClient client, EntityId entityId, EntityListener entityListener, PayloadFormat<Boolean> stateSerializer) {
        super(client, entityId, entityListener, stateSerializer);
    }

}
