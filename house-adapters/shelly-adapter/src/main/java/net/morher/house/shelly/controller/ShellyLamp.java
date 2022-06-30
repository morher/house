package net.morher.house.shelly.controller;

import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.entity.light.LightOptions;
import net.morher.house.api.entity.light.LightState;
import net.morher.house.api.entity.light.LightState.PowerState;
import net.morher.house.api.entity.light.LightStateHandler;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttTopicManager;
import net.morher.house.api.mqtt.payload.BooleanMessage;

public class ShellyLamp {
    private final MqttTopicManager<Boolean> stateTopic;
    private final MqttTopicManager<Boolean> commandTopic;
    private LightStateHandler handler;

    public ShellyLamp(HouseMqttClient mqtt, String nodeName, int relayIndex, LightEntity lightEntity) {
        String relayTopic = "shellies/" + nodeName + "/relay/" + relayIndex;
        commandTopic = new MqttTopicManager<>(mqtt, relayTopic + "/command", BooleanMessage.onOffLowerCase(), null);
        stateTopic = new MqttTopicManager<>(mqtt, relayTopic, BooleanMessage.onOffLowerCase(), this::onRelayStateChanged);

        handler = new LightStateHandler(lightEntity, this::onLightState);
        lightEntity.setOptions(new LightOptions(false, null));

        stateTopic.activateSubscription(true);
    }

    public void onLightState(LightState lampState) {
        commandTopic.publish(PowerState.ON.equals(lampState.getState()), false);
    }

    public void onRelayStateChanged(String topic, Boolean data, int qos, boolean retained) {
        handler.updateState(new LightState(data ? PowerState.ON : PowerState.OFF, null, null));
    }
}
