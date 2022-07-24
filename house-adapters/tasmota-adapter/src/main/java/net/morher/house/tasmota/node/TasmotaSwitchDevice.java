package net.morher.house.tasmota.node;

import lombok.RequiredArgsConstructor;
import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.switches.SwitchEntity;
import net.morher.house.api.entity.switches.SwitchOptions;
import net.morher.house.api.entity.switches.SwitchStateHandler;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttTopicManager;
import net.morher.house.api.mqtt.payload.BooleanMessage;

public class TasmotaSwitchDevice {
    private final RelayChannel relayChannel;

    public TasmotaSwitchDevice(String node, HouseMqttClient client, int relay, SwitchEntity entity) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setManufacturer("Tasmota");
        new SwitchStateHandler(entity, deviceInfo, this::onSwitchState);
        entity.setOptions(new SwitchOptions());

        relayChannel = new RelayChannel(client.topic("cmnd/" + node + "/POWER" + relay, BooleanMessage.onOff()));
    }

    public void onSwitchState(Boolean switchState) {
        relayChannel.updateState(switchState);
    }

    @RequiredArgsConstructor
    private class RelayChannel {
        private final MqttTopicManager<Boolean> tasmotaCommandTopic;

        public void updateState(Boolean state) {
            tasmotaCommandTopic.publish(state, false);
        }
    }

}
