package net.morher.house.shelly.controller;

import net.morher.house.api.entity.switches.SwitchEntity;
import net.morher.house.api.entity.switches.SwitchOptions;
import net.morher.house.api.entity.switches.SwitchStateHandler;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttTopicManager;
import net.morher.house.api.mqtt.payload.BooleanMessage;
import net.morher.house.api.subscription.Subscription;

public class ShellySwitch {
    private final MqttTopicManager<Boolean> commandTopic;
    private final Subscription stateSubscription;
    private final SwitchStateHandler handler;

    public ShellySwitch(HouseMqttClient mqtt, String nodeName, int relayIndex, SwitchEntity entity) {
        String relayTopic = "shellies/" + nodeName + "/relay/" + relayIndex;
        commandTopic = mqtt.topic(relayTopic + "/command", BooleanMessage.onOffLowerCase(), false);
        stateSubscription = mqtt.topic(relayTopic, BooleanMessage.onOffLowerCase()).subscribe(this::onRelayStateChanged);

        handler = new SwitchStateHandler(entity, this::onLightState);
        entity.setOptions(new SwitchOptions());
    }

    public void onLightState(Boolean lampState) {
        commandTopic.publish(lampState, false);
    }

    public void onRelayStateChanged(String topic, Boolean data, int qos, boolean retained) {
        handler.updateState(data);
    }

}
